package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.models.DataFetcher;
import edu.sdccd.cisc191.common.entities.Stock;

//import org.springframework.stereotype.Controller;
/*import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;*/
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PostMapping;
import edu.sdccd.cisc191.common.entities.Stock;

/*
* (String newTicker, String newName, String newDescription,
                 String newSector, double newPrice, double newDividend)
* */

@RequestMapping(DataFetcher.apiEndpointURL)
@RestController
public class StockController {

    //Dummy Data
    private Stock[] stocks = {
          new Stock("AAPL", "Apple", "Apple is expensive!", "Technology", 400.12, 0.5),
          new Stock("MCST", "Microsoft", "Microsoft is also expensive but not quite as expensive as Apple.", "Technology", 240.15, 0.25),
          new Stock("APCX", "AppTech Payments Corp.", "AppTech is a Fintech company.", "Financial Technology", 2.25, 0.15),
          new Stock("MVCN", "Marvelous Random Name", "Now I'm really just making stuff up", "BS", 3.33, 0.67),
          new Stock("AAA", "AAA", "We use this whenever our car sucks.", "Auto", 40.56, 0.04),
          new Stock("SBRU", "Subaru", "Great cars!", "Auto", 300.45, 0.06),
          new Stock("FDSC", "Federal Screw Works", "Let us do the screwing for you.", "Screws", 249.35, 0.06),
          new Stock("BOIL", "PROSHARES ULTRA BLOOMBERG NA", "dksjlafsdkfjalsdfjas", "Financial", 345.67, 0.96),
          new Stock("MHUA", "MEIHUA INTERNATIONAL MEDICAL", "ksdkfjdajsdfasldfasdkfjd", "Medical", 888.99, 0.2),
          new Stock("XPNGF", "XPENG INC - CLASS A SHARES", "dkjsfkdaljfskdjaflsdfjaslkdfajsd", "Financial", 789.45, 0.5)
    };

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

    @GetMapping("/stocks")
    public String getStocks() {
        return "stocks";
    }

    @PostMapping("/stocks")
    public void addStock(String ticker) {

    }

    @PutMapping("/stocks")
    public void updateStock(String ticker) {

    }

    @DeleteMapping("stocks")
    public void deleteStock(String ticker) {

    }
}
