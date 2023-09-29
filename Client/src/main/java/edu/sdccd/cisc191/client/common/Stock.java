package edu.sdccd.cisc191.client.common;

public class Stock {
    String ticker;

    String name;

    String description;

    double sharePrice;

    double dividendYield;

    String stockSector;

    public Stock(){}


    public Stock(String newTicker) {
        setTicker(newTicker);

    }

    public Stock(String newTicker, String newName, String newDescription,
                 String newSector, double newPrice, double newDividend){
        setTicker(newTicker);
        setName(newName);
        setDescription(newDescription);
        setDividend(newDividend);
        setSector(newSector);
        setPrice(newPrice);
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return sharePrice;
    }

    public void setPrice(double price) {
        this.sharePrice = price;
    }

    public double getDividend() {
        return dividendYield;
    }

    public void setDividend(double dividend) {
        this.dividendYield = dividend;
    }

    public String getSector() {
        return stockSector;
    }

    public void setSector(String name) {
        this.stockSector = name;
    }
}
