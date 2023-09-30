package edu.sdccd.cisc191.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.client.common.Stock;
import edu.sdccd.cisc191.client.models.DataFetcher;
import edu.sdccd.cisc191.common.entities.StockCandle;

//import org.springframework.stereotype.Controller;
/*import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;*/
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;
//import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/*
* (String newTicker, String newName, String newDescription,
                 String newSector, double newPrice, double newDividend)
* */

@RequestMapping(DataFetcher.apiEndpointURL)
@RestController
public class StockController implements DataFetcher {
    String[] myTickers = {"AAPL","MSFT","NVDA","AMZN","COST","TSM","META","TSLA"};
    //Dummy Data to initialize UIStock objects
    public ArrayList<Stock> stocks = new ArrayList<>() {
        {
            for (String ticker : myTickers) {
                try {
                    add(new Stock(ticker));
                } catch (MalformedURLException | JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    };

    //Convert the above dummy data into a List so we can perform CRUD
    //operations on them.
//    public static final String stockRepositoryAddress = "stockrepo.txt";

    //CRUD Get all stocks
    @GetMapping("/stocks")
    public List<Stock> getAll() {
        return stocks;
    }

    //CRUD Create a new stock and add to stocks List
    @PostMapping("/stocks")
    public void create(@RequestBody Stock stock) {
        Stock newStock = new Stock(
            stock.getId(), stock.getTicker(), stock.getName(),
            stock.getDescription(), stock.getSector(), stock.getPrice(),
            stock.getDividend()
        );
        this.stocks.add(newStock);
    }

    //CRUD Get a single stock
    @GetMapping("/stocks/{id}")
    public Stock getSingle(@PathVariable Long id) {
        for (Stock stock : stocks) {
            if (stock.getId().equals(id)) {
                return stock;
            }
        }
        return null;
    }

    @PutMapping("/stocks/{id}")
    public void update(@RequestBody Stock updatedStock, @PathVariable Long id) {
        for (Stock stock : stocks) {
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

    @DeleteMapping("/stocks/{id}")
    public void delete(@PathVariable Long id) {
        for (Stock stock : stocks) {
            if (stock.getId().equals(id)) {
                stocks.remove(stock);
                System.out.println("Successfully deleted stock.");
                return;
            }
        }
    }

    @GetMapping("/stocks/candles/{ticker}")
    public double[][] getCandles(@PathVariable String ticker) {
        StockCandle candles;

        try {
            candles = new StockCandle(ticker);
        } catch(Exception e) {
            System.err.println(e);
            return null;
        }

        double[][] data = candles.getStockInfo();
        System.out.println(candles.toString());
        return data;
    }
}
