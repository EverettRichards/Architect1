package edu.sdccd.cisc191.common.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// This class is used to view the contents of All Tickers or Default Tickers (text files) WITHOUT static methods.
public class TickerReader {
    private static final String defaultTickersFileAddress = "src/main/static_data/default_tickers.txt";
    private static final String allTickersFileAddress = "src/main/static_data/all_tickers.txt";

    private String[] defaultTickers = new String[0];
    private String[] allTickers = new String[0];

    private void updateDefaultTickers(){
        if (defaultTickers.length>1){ return; }
        try {
            defaultTickers = Files.lines(Paths.get(defaultTickersFileAddress))
                    .collect(Collectors.toList())
                    .toArray(new String[0]);
        } catch (IOException e) {
            System.out.println(e.toString());
            defaultTickers = new String[]{"AAPL"};
        }
    }

    public void updateAllTickers(){
        if (allTickers.length>1){ return; }
        try {
            allTickers = Files.lines(Paths.get(allTickersFileAddress))
                    .collect(Collectors.toList())
                    .toArray(new String[0]);
        } catch (IOException e) {
            System.out.println(e.toString());
            allTickers = new String[]{"AAPL"};
        }
    }

    public TickerReader(){
        updateDefaultTickers();
        updateAllTickers();
    }

    // Returns a list of all Default Tickers (in the user's portfolio by default)
    public String[] getDefaultTickers(){
        updateDefaultTickers();
        return defaultTickers;
    }

    // Returns a list of all valid tickers in US Stock Exchanges on FinnHub
    public String[] getAllTickers(){
        updateAllTickers();
        return allTickers;
    }
}
