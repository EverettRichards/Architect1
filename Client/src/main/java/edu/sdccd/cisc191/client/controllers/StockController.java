package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.models.DefaultStocksFileIO;
import edu.sdccd.cisc191.client.models.RankedResult;
import edu.sdccd.cisc191.client.models.StockDataFetcher;
import edu.sdccd.cisc191.client.models.UserDataFetcher;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import edu.sdccd.cisc191.common.entities.StockList;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import java.util.concurrent.*;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * StockController*
 * Handles the views for a user's dashboard of stock data
 * as well as the interactivity with the data.
 */
@Controller
public class StockController implements DataFetcher {
    RestTemplate restTemplate = new RestTemplate();
    String resourceURL = DataFetcher.backendEndpointURL + DataFetcher.apiEndpointURL;

    /**
     * Sets up to display the dashboard, which displays a list of stocks the user follows.
     * @param model model to display stocks
     * @return dashboard the dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        StockList stockList = new StockList(); //List of stocks the user follows.

        //For getting user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        HttpSession session = request.getSession();

        User user;

        //For use in html
        LinkedList<Stock> stocks;
        if (session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
        } else {
            try {
                user = UserDataFetcher.get(currentPrincipalName);
            } catch (Exception error) {
                model.addAttribute("error", "Something went wrong!");
                return "/dashboard";
            }

            session.setAttribute("user", user);
        }

        if(stockList.length() < 1) {
            user.getFollowedTickers().parallelStream().forEach(ticker -> {
                Stock fetchStock = StockDataFetcher.get(ticker);
                stockList.add(fetchStock);
            });
        }

        stocks = stockList.getStocks();

        if(user.getFollowedTickers().isEmpty()) {
            stocks = null;
        }

        model.addAttribute("stocks", stocks);

        return "dashboard";
    }

    /**
     * Sets up to display the stock page
     * @param ticker the Long id that identifies each stock
     * @param model the method to create the stock listing
     * @return stock the stock page
     */
    @GetMapping("/dashboard/stock/{ticker}")
    public String stockDetails(@PathVariable("ticker") String ticker, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List userTickers = user.getFollowedTickers();
        Boolean userHasTicker = false;

        //Stock variable for passing into template
        Stock stock;

        try {
            stock = StockDataFetcher.get(ticker);
        } catch (Exception e) {
            stock = null;
        }

        if(userTickers.contains(stock.getTicker())) {
            userHasTicker = true;
        }
        model.addAttribute("userHasTicker", userHasTicker);
        model.addAttribute("stock", stock);
        return "stock";
    }

    /**
     * Removes a stock from user's followed tickers.
     * @param ticker the Long id that identifies each stock
     * @param model the method to create the stock listing
     * @return redirect to dashboard
     */
    @GetMapping("/dashboard/delete_stock/{ticker}")
    public String deleteFollowedStock(@PathVariable("ticker") String ticker, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<String> usersTickers = user.getFollowedTickers();
        usersTickers.remove(ticker);
        user.setFollowedTickers(usersTickers);

        try {
            UserDataFetcher.update(user);
        } catch (Exception error) {
            model.addAttribute("error", "There was an error processing your request.");
            return "dashboard";
        }
//        this.stockList.delete(ticker);
        session.setAttribute("user", user);
        return "redirect:/dashboard";
    }

    /**
     * Removes a stock from user's followed tickers.
     * @param ticker the Long id that identifies each stock
     * @param model the method to create the stock listing
     * @return redirect to dashboard
     */
    @GetMapping("/dashboard/add_stock/{ticker}")
    public String addFollowedStock(@PathVariable("ticker") String ticker, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<String> usersTickers = user.getFollowedTickers();
        usersTickers.add(ticker);
        user.setFollowedTickers(usersTickers);

        try {
            UserDataFetcher.update(user);
        } catch (Exception error) {
            model.addAttribute("error", "There was an error processing your request.");
            return "dashboard";
        }

        Stock stock;
        try {
            stock = StockDataFetcher.get(ticker);
        } catch (Exception e) {
            stock = null;
        }
//        this.stockList.add(stock);
        session.setAttribute("user", user);
        return "redirect:/dashboard";
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("query", "...");
        model.addAttribute("stocks", new ArrayList<String>());
        return "search";
    }

    @GetMapping("/search/{query}")
    public String searchWithQuery(@PathVariable("query") String query, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<String> usersTickers = user.getFollowedTickers();

        List<ExtractedResult> topResults = FuzzySearch.extractTop(query, allTickers, 10);

        ArrayList<RankedResult> rankedResultList = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(topResults.size());
        CompletionService<RankedResult> completionService = new ExecutorCompletionService<>(executor);

        int totalStockOptions = 0;

        for(ExtractedResult result : topResults) {
            if(result.getScore() < 50) {
                break;
            }

            totalStockOptions += 1;

            completionService.submit(() -> {
                try {
                    String ticker = result.getString();
                    System.out.println(result.getScore());
                    Stock stock;
                    stock = StockDataFetcher.get(ticker);
                    RankedResult rankedResult = new RankedResult(result.getScore(), stock);
                    return rankedResult;
                } catch (Exception e) {
                    System.err.println(e);
                    return null;
                }
            });
        }

        int received = 0;
        while(received < totalStockOptions) {
            try {
                RankedResult rankedResult = completionService.take().get();
                if(rankedResult.getStock().getName() != null) {
                    rankedResultList.add(rankedResult);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }

            received++;
        }

        rankedResultList.sort(Comparator.comparing(RankedResult::getScore).reversed());

        // extract stock from rankedResult from rankedResultList
        List<Stock> stockList = rankedResultList.stream().map(RankedResult::getStock).collect(Collectors.toList());

        // shutdown threads
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error shutting down threads: " + e);
        }

        model.addAttribute("query", query);
        model.addAttribute("stocks", stockList);
        model.addAttribute("usersTickers", usersTickers);
        return "search";
    }
}