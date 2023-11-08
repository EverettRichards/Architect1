package edu.sdccd.cisc191.common.entities;

import java.util.ArrayList;

public class StockArrayList {
    private ArrayList<String> tickers;

    public ArrayList<String> getTickers() { return tickers; }

    public void setTickers(ArrayList<String> newTickers){
        tickers = newTickers;
    }

    public boolean addTicker(String ticker){
        if (!tickers.contains(ticker)){
            tickers.add(ticker);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeTicker(String ticker){
        if (tickers.contains(ticker)){
            tickers.remove(ticker);
            return true;
        } else {
            return false;
        }
    }

    public String[] toArray(){
        String[] output = new String[tickers.size()];
        for (int i = 0; i < tickers.size(); i++){
            output[i] = tickers.get(i);
        }
        return output;
    }

    public String toString(){
        return String.join(", ",tickers);
    }
}
