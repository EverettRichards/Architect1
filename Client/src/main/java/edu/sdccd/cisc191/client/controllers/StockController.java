package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.DataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedList;
import edu.sdccd.cisc191.common.entities.StockList;

/**
 * StockController
 *
 * Handles the views for a user's dashboard of stock data
 * as well as the interactivity with the data.
 */
@Controller
public class StockController implements DataFetcher {

    public StockList stocks; //List of stocks the user follows.
    RestTemplate restTemplate = new RestTemplate();
    String resourceURL = DataFetcher.backendEndpointURL + DataFetcher.apiEndpointURL;

    /**
     * Sets up to display the dashboard, which displays a list of stocks the user follows.
     * @param model model to display stocks
     * @return dashboard the dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        LinkedList<Stock> stockList;
        //restTemplate.getForObject
        try {
            ResponseEntity<LinkedList<Stock>> response = restTemplate.exchange(
                    resourceURL + "/stocks",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            stockList = response.getBody();
            this.stocks = new StockList(stockList);
        } catch (Exception e) {
            this.stocks = null;
        }

        model.addAttribute("stocks", this.stocks);

        return "dashboard";
    }

    /**
     * Sets up to display the stock page
     * @param id the Long id that identifies each stock
     * @param model the method to create the stock listing
     * @return stock the stock page
     */
    @GetMapping("/dashboard/stock/{id}")
    public String stockDetails(@PathVariable("id") Long id, Model model) {
        //Stock variable for passing into template
        Stock stock;

        try {
            ResponseEntity<Stock> response = restTemplate.exchange(
                    resourceURL + "/stocks/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            stock = response.getBody();
        } catch (Exception e) {
            stock = null;
        }

        model.addAttribute("stock", stock);
        return "stock";
    }

    @GetMapping("/dashboard/delete_stock/{id}")
    @ResponseBody
    public void deleteStock(@PathVariable("id") Long id) {
        this.stocks.remove(id);
    }

}