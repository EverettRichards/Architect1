package edu.sdccd.cisc191.server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;

//import org.springframework.stereotype.Controller;
/*import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;*/

import edu.sdccd.cisc191.server.*;

import edu.sdccd.cisc191.server.errors.BadTickerException;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/*
* (String newTicker, String newName, String newDescription,
                 String newSector, double newPrice, double newDividend)
* */

/**
 * StockController a class to generate the data to display on the client webpage
 */
@RestController
@RequestMapping("/api")
public class StockController {
    /**
     * Lists all the stocks to display on the webpage
     * @return stocks the stocks that are being watched
     */
    //CRUD Get all stocks
    // @GetMapping("/stocks")
    // public List<Stock> getAll() {
    //     return stocks;
    // }

    @GetMapping("/stock/{ticker}")
    public Stock getAll(@PathVariable String ticker) {
        ServerStock serverStock;
        try {
            return new ServerStock(ticker);
        } catch (MalformedURLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (BadTickerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the candle data and puts it into a ticker format to be displayed
     * @param ticker the stock information
     * @return data the candle data in 2d array
     */
    @GetMapping("/stocks/candles/{ticker}")
    public Number[][] getCandles(@PathVariable String ticker) {
        StockCandle candles;

        try {
            candles = new ServerStockCandle(ticker,"day");
        } catch(Exception e) {
            System.err.println(e);
            return null;
        }

        Number[][] data = candles.getStockInfo();

        return data;
    }
}
