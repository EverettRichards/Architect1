package edu.sdccd.cisc191.common.entities;

import edu.sdccd.cisc191.common.errors.BadTickerException;

import java.util.Locale;

/*
This class is used for validating "Tickers", which are
short unique strings that represent companies on a stock exchange.

Includes error handling for invalid tickers.
 */

public class Ticker {
    private String ticker;

    public String getTicker(){
        return ticker;
    }

    TickerReader tickerReader = new TickerReader();

    // Verify if a user-provided ticker actually exists before proceeding.
    public boolean isValidTicker(){
        String[] allTickers = tickerReader.getAllTickers();

        for (String thisTicker : allTickers){
            if (thisTicker.equals(ticker)){
                return true;
            }
        }

        return false;
    }

    // Constructor. Creates new Ticker and checks to see if it is valid.
    public Ticker(String ticker) throws BadTickerException {
        this.ticker = ticker.toUpperCase(Locale.ROOT);
        if (!isValidTicker()) {
            throw new BadTickerException(ticker);
        }
    }
}
