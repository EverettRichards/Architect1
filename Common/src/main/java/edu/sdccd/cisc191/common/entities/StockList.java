package edu.sdccd.cisc191.common.entities;

import java.util.Collections;
import java.util.LinkedList;

/**
 * StockList a program to create a list of stocks to be used to display on the webpage
 */
public class StockList {
    private LinkedList<Stock> stocks; //the stocks that will make up the list of stocks

    /**
     * Constructor creates a new StockList
     * no-args constructor, just initializes a new LinkedList<Stock>
     * instance variable for stocks.
     */
    public StockList() {
        this.stocks = new LinkedList<>();
    }

    /**
     * Constructor creates a new StockList from data
     * @param stocks - a linked list of stocks to initialize the stocks
     * instance variable.
     */
    public StockList(LinkedList<Stock> stocks) {
        this.stocks = stocks;
        this.sort();
    }

    //Class Methods

    /**
     * getStocks
     * no args
     * returns stocks
     */
    public LinkedList<Stock> getStocks() {
        return this.stocks;
    }

    /**
     * addStock
     * @param newStock
     * Adds newStock to the linked list stocks and
     * sorts list.
     */
    public int addStock(Stock newStock) {
        boolean success = this.stocks.add(newStock);
        if(success) {
            this.sort();
            return 0;
        }
        return -1;
    }

    /**
     * removeStock
     * @param id
     * Takes the variable id and uses it to
     * find a stock in the stocks list.  If successful,
     * the stock is removed from the linked list and returns 0.
     * If unsuccessful, the method returns -1.
     */
    public int removeStock(Long id) {
        for (Stock stock : stocks) {
            if (stock.getId() == id) {
                stocks.remove(stock);
                return 0;
            }
        }
        return -1;
    }

    /**
     * sort
     * no-args
     * Sorts the stocks linked list by ticker name alphabetically.
     */
    public void sort() {
        Collections.sort(this.stocks);
    }

    /**
     * length
     * no-args
     * Returns the number of items in the stocks linked list as an integer.
     */
    public int length() {
        return this.stocks.size();
    }
}
