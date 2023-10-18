package edu.sdccd.cisc191.server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;

//import org.springframework.stereotype.Controller;
/*import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;*/
import edu.sdccd.cisc191.server.FinnhubNetworking;
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

/**
 * StockController a class to generate the data to display on the client webpage
 */
@RequestMapping("/api")
@RestController
public class StockController {
    String[] myTickers = {"AAPL","DIS","BAC","UAA","CCL","KO","WMT","T","GOOGL","MSFT","V","NVDA","AMZN","COST","AMD","TSM","META","TSLA"};
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

    /**
     * Lists all the stocks to display on the webpage
     * @return stocks the stocks that are being watched
     */
    //CRUD Get all stocks
    @GetMapping("/stocks")
    public List<Stock> getAll() {
        return stocks;
    }

    /**
     * Creates a new stock to be added to the website
     * @param stock the new stock created
     */
    //CRUD Create a new stock and add to stocks List
    @PostMapping("/stocks")
    public void create(@RequestBody Stock stock) {
        Stock newStock = new Stock(
            stock.getTicker(), stock.getName(),
            stock.getDescription(), stock.getSector(), stock.getPrice(),
            stock.getDividend()
        );
        this.stocks.add(newStock);
    }

    /**
     * Gets the information for a single stock
     * @param id the Long id of the stock to get its information
     * @return stock the stock with that id or null if no match
     */
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

    /**
     * Updates the stock info with new price data
     * @param updatedStock the new stock price data
     * @param id the id of the stock to update
     */
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

    /**
     * Deletes a stock from display on the webpage, so you don't follow it anymore
     * @param id the long id to identify the stock to delete
     */
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

    /**
     * Gets the candle data and puts it into a ticker format to be displayed
     * @param ticker the stock information
     * @return data the candle data in 2d array
     */
    @GetMapping("/stocks/candles/{ticker}")
    public double[][] getCandles(@PathVariable String ticker) {
        StockCandle candles;

        try {
            candles = FinnhubNetworking.newStockCandle(ticker);
        } catch(Exception e) {
            System.err.println(e);
            return null;
        }

        double[][] data = candles.getStockInfo();
        //System.out.println(candles.toString());
        return data;
    }
}
