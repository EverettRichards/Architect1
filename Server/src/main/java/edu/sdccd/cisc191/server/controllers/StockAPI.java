package edu.sdccd.cisc191.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import edu.sdccd.cisc191.server.Finnhub;
import edu.sdccd.cisc191.server.database.StockServiceJpa;
import edu.sdccd.cisc191.server.database.Stock;

@RestController
@RequestMapping("/api")
public class StockAPI {
    // @Autowired
    // private StockServiceJpa stockServiceJpa;

    @GetMapping(path="/candles")
    public String getCandles(@RequestBody Stock stock) {
        Finnhub temp = new Finnhub("asdf");
        temp.getCandle("asdf");

        // this.stockServiceJpa.addStock(stock);

        return temp.getCandle("asdf");
    }
}
