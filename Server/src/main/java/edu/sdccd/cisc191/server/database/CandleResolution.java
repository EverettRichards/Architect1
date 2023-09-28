package edu.sdccd.cisc191.server.database;

import java.util.ArrayList;

import javax.persistence.Embeddable;

@Embeddable
public class CandleResolution {
    // https://finnhub.io/docs/api/stock-candles
    private Float high;
    private Float open;
    private Float closest;
    private Float low;
    private Float time;
}
