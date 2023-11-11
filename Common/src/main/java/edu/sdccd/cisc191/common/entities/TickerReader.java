package edu.sdccd.cisc191.common.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// This class is used to view the contents of All Tickers or Default Tickers (text files) WITHOUT static methods.
public class TickerReader {
    private static final String defaultTickersFileAddress = "Common/src/main/static_data/default_tickers.txt";
    private static final String allTickersFileAddress = "Common/src/main/static_data/all_tickers.txt";

    private String[] defaultTickers = new String[0];
    private String[] allTickers = new String[0];

    public String[] getDefaultTickers(){
        if (defaultTickers.length == 0) {
            try {
                defaultTickers = Files.lines(Paths.get(defaultTickersFileAddress))
                        .collect(Collectors.toList())
                        .toArray(new String[0]);
            } catch (IOException e) {
                defaultTickers = new String[]{"AAPL"};
            }
        }
        return defaultTickers;
    }


    public String[] getAllTickers(){
        if (allTickers.length == 0) {
            try {
                allTickers = Files.lines(Paths.get(allTickersFileAddress))
                        .collect(Collectors.toList())
                        .toArray(new String[0]);
            } catch (IOException e) {
                allTickers = new String[]{"AAPL"};
            }
        }
        return allTickers;
    }
}
