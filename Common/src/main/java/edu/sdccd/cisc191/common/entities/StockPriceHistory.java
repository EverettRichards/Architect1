package edu.sdccd.cisc191.common.entities;

import java.sql.Time;

/**
 * StockPriceHistory is a program that keeps track of the stock prices over time
 */
public class StockPriceHistory {
    private Stock stock;                //the stock to track the price history
    private float price;                //the price of the stock

    private Time time;                  // the time frame used

    private StockPriceHistory prev;     //the previous time period to track
    private StockPriceHistory next;     //the next time period to use

}
