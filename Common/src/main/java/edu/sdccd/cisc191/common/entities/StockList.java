package edu.sdccd.cisc191.common.entities;

import java.util.Collections;
import java.util.LinkedList;

/**
 * StockList a program to create a list of stocks to be used to display on the webpage
 */
public class StockList {
    private LinkedList<Stock> stocks;         //the stocks that will make up the list of stocks

    /**
     * Constructor creates a new StockList
     */
    public StockList(LinkedList<Stock> stocks) {
        this.stocks = stocks;
        this.sort();
    }

    public LinkedList<Stock> getStocks() {
        return this.stocks;
    }

    public void add(Stock newStock) {
        this.stocks.add(newStock);
        this.sort();
    }

    public int remove(Long id) {
        for (Stock stock : stocks) {
            if (stock.getId() == id) {
                stocks.remove(stock);
                return 0;
            }
        }
        return -1;
    }

    public void sort() {
        Collections.sort(this.stocks);
    }

}
