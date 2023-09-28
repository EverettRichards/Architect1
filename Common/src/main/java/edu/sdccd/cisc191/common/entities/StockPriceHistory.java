package edu.sdccd.cisc191.common.entities;

import java.sql.Time;

public class StockPriceHistory {
    private Stock stock;
    private float price;

    private Time time;

    private StockPriceHistory prev;
    private StockPriceHistory next;

}
