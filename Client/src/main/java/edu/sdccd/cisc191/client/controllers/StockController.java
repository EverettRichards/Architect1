package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.models.DataFetcher;
import edu.sdccd.cisc191.client.models.UIStock;

//import org.springframework.stereotype.Controller;
/*import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;*/
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

/*
* (String newTicker, String newName, String newDescription,
                 String newSector, double newPrice, double newDividend)
* */

@RequestMapping(DataFetcher.apiEndpointURL)
@RestController
public class StockController implements DataFetcher {

    //Dummy Data to initialize UIStock objects
    public UIStock[] stocksArray = {
          new UIStock(1L, "AAPL", "Apple", "Apple is expensive!", "Technology", 400.12, 0.5),
          new UIStock(2L, "MCST", "Microsoft", "Microsoft is also expensive but not quite as expensive as Apple.", "Technology", 240.15, 0.25),
          new UIStock(3L, "APCX", "AppTech Payments Corp.", "AppTech is a Fintech company.", "Financial Technology", 2.25, 0.15),
          new UIStock(4L, "MVCN", "Marvelous Random Name", "Now I'm really just making stuff up", "BS", 3.33, 0.67),
          new UIStock(5L, "AAA", "AAA", "We use this whenever our car sucks.", "Auto", 40.56, 0.04),
          new UIStock(6L, "SBRU", "Subaru", "Great cars!", "Auto", 300.45, 0.06),
          new UIStock(7L, "FDSC", "Federal Screw Works", "Let us do the screwing for you.", "Screws", 249.35, 0.06),
          new UIStock(8L, "BOIL", "PROSHARES ULTRA BLOOMBERG NA", "dksjlafsdkfjalsdfjas", "Financial", 345.67, 0.96),
          new UIStock(9L, "MHUA", "MEIHUA INTERNATIONAL MEDICAL", "ksdkfjdajsdfasldfasdkfjd", "Medical", 888.99, 0.2),
          new UIStock(10L, "XPNGF", "XPENG INC - CLASS A SHARES", "dkjsfkdaljfskdjaflsdfjaslkdfajsd", "Financial", 789.45, 0.5)
    };

    //Convert the above dummy data into a List so we can perform CRUD
    //operations on them.
    private List<UIStock> stocks = Arrays.asList(stocksArray);

    /*@GetMapping("/stocks")
    public String showStocks() {
        return "stocks";
    }
    @PostMapping("/stocks")
    public String addStock(@Validated Stock stock, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "stocks";
        }

        // TODO: call server
        return "redirect:/stocks";
    }*/
    public static final String stockRepositoryAddress = "stockrepo.txt";

    //CRUD Get all stocks
    @GetMapping("/stocks")
    public List<UIStock> getAll() {
        return stocks;
    }

    //CRUD Create a new stock and add to stocks List
    @PostMapping("/stocks")
    public void create(@RequestBody UIStock stock) {
        UIStock newStock = new UIStock(stock.getId(), stock.getTicker(), stock.getName(),
                stock.getDescription(), stock.getSector(), stock.getPrice(),
                stock.getDividend());
        this.stocks.add(newStock);
    }

    //CRUD Get a single stock
    @GetMapping("/stocks/{id}")
    public String getSingle(@PathVariable Long id) {
        for (UIStock stock : stocks) {
            if (stock.getId().equals(id)) {
                return stock.toString();
            }
        }
        return "Stock not found.";
    }

    @PutMapping("/stocks/{id}")
    public void update(@RequestBody UIStock updatedStock, @PathVariable Long id) {
        for (UIStock stock : stocks) {
            if (stock.getId().equals(id)) {
                stock.setTicker(updatedStock.getTicker());
                stock.setName(updatedStock.getName());
                stock.setDescription(updatedStock.getDescription());
                stock.setSector(updatedStock.getSector());
                stock.setPrice(updatedStock.getPrice());
                stock.setDividend(updatedStock.getDividend());

                System.out.println("Successfully updated stock.");
            }
        }
    }

    @DeleteMapping("stocks/{id}")
    public void delete(@PathVariable Long id) {
        for (UIStock stock : stocks) {
            if (stock.getId().equals(id)) {
                stocks.remove(stock);
                System.out.println("Successfully deleted stock.");
            }
        }
    }
}
