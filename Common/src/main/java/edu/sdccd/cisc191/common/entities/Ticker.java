package edu.sdccd.cisc191.common.entities;

import edu.sdccd.cisc191.common.errors.BadTickerException;

import java.util.Locale;

public class Ticker {
    private String ticker;

    public String getTicker(){
        return ticker;
    }

    TickerReader tickerReader = new TickerReader();

    public boolean isValidTicker(){
        String[] allTickers = tickerReader.getAllTickers();

        for (String thisTicker : allTickers){
            if (thisTicker.equals(ticker)){
                return true;
            }
        }

        return false;
    }

    public Ticker(String ticker) throws BadTickerException {
        this.ticker = ticker.toUpperCase(Locale.ROOT);
        if (!isValidTicker()) {
            throw new BadTickerException(ticker);
        }
    }
}
