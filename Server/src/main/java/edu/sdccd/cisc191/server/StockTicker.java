package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.entities.Stock;

public class StockTicker extends Stock {
    public String companyName;
    public String stockSymbol;
    public double stockPrice;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockSymbol(){
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    //TODO other stock ticker methods
}

