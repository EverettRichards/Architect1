package edu.sdccd.cisc191.client.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {
    //@JsonProperty("id")
    private Long id;

    private String ticker;
    private String name;
    private String description;
    private double sharePrice;
    private double dividendYield;
    private String stockSector;

    public Stock(long id, String ticker, String newName, String description,
                 String sector, double price, double dividend){
        this.id = id;
        this.ticker = ticker;
        this.name = newName;
        this.description = description;
        this.sharePrice = price;
        this.dividendYield = dividend;
        this.stockSector = sector;
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

    public Long getId() {
        return this.id;
    }
}
