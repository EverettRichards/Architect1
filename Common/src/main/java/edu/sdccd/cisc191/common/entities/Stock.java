package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.common.entities.Requests;

import java.net.MalformedURLException;
//import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Stock class that is used to create objects displaying the current
 * information about stocks being traded on the Exchange
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {
    //@JsonProperty("id")
    private Long id;
    private String ticker;          //the stock ticker or symbol listed on the exchange
    private String name;            //the company name that issued the stock
    private String description;     //description of the company issuing the stock
    private double sharePrice;      //the stock price of one share
    private double dividendYield;   //the yield of any dividends the company issues
    private String stockSector;     //the sector or market that the company operates in

    private long lastRefreshTime;

    private long secondsBeforeRefreshNeeded = 60; // number of seconds before a cached stock will be forced to refresh

    public static long lastId = 0L;  //Initialize an id on startup.  Every Stock object created adds 1 to this and uses that for its own id.

    /**
     * Default constructor for the Stock class
     */
    public Stock () {
    }

    public Stock(String ticker, String newName, String description,
                 String sector, double price, double dividend){
        id = ++lastId;
        this.id = id;
        this.ticker = ticker;
        this.name = newName;
        this.description = description;
        this.sharePrice = price;
        this.dividendYield = dividend;
        this.stockSector = sector;
    }

    /**
     * Most comprehensive constructor available. Allows for full override of all
     * core attributes of a stock, including "id" and "lastRefreshTime".
     * This constructor is used to read a stock from memory.
     */
    public Stock(long newId, long lastRefresh, String ticker, String newName, String description,
                 String sector, double price, double dividend) throws MalformedURLException, JsonProcessingException {
        this(ticker,newName,description,sector,price,dividend);
        this.id = newId;
        this.lastRefreshTime = lastRefresh;
        long currentTime = System.currentTimeMillis();
    }

    /**
     * Updates a stock with real-time information from the Finnhub API
     */
    public void Update() throws JsonProcessingException, MalformedURLException {
        // Get the JSON data from Finnhub with basic company info such as name, sector, etc.
        String jsonInput = Requests.get("https://finnhub.io/api/v1/stock/profile2?symbol="
                + ticker + "&token=" + DataFetcher.finnhubKey);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonInput);

        lastRefreshTime = System.currentTimeMillis();

        // Update core attributes accordingly
        name = rootNode.get("name").asText();
        stockSector = rootNode.get("finnhubIndustry").asText();
        description = String.format("%s is a company in the %s sector.",name,stockSector);

        // Get the JSON data with FINANCIAL info such as price
        String jsonInput2 = Requests.get("https://finnhub.io/api/v1/quote?symbol="
                + ticker + "&token=" + DataFetcher.finnhubKey);
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode rootNode2 = mapper2.readTree(jsonInput2);

        // Update core attributes accordingly
        sharePrice = rootNode2.get("c").asDouble();
    }

    public Stock(String newTicker) throws MalformedURLException, JsonProcessingException {
        // Internally assign the new Ticker value
        ticker = newTicker;
        lastRefreshTime = 0L;
    }

    /**
     * Gets the stock ticker symbol
     * @return ticker the company symbol on the exchange
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * Sets the company stock ticker symbol
     * @param ticker the symbol listed on the exchange
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /**
     * Gets the company name of the stock
     * @return name the company name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the company name for the stock being created or updated
     * @param name company name of the stock
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The company description to provide basic info on stocks
     * @return description the company description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the information that describes what the company does
     * @param description the company description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the share price of the stock
     * @return sharePrice the cost of a share of stock
     */
    public double getPrice() {
        return sharePrice;
    }

    /**
     * Gets the time stamp (in ms) of the last time this stock was updated with real-time data
     * @return lastRefreshTime the time stamp of the last time the stock was refreshed
     */
    public long getLastRefresh() { return lastRefreshTime; }

    public void setLastRefresh(long num) { lastRefreshTime = num; }

    /**
     * Sets the share price of the stock
     * @param price the price of a stock share
     */
    public void setPrice(double price) {
        this.sharePrice = price;
    }

    /**
     * Gets the value of any dividends that the company issues
     * @return dividendYield the amount of the dividend
     */
    public double getDividend() {
        return dividendYield;
    }

    /**
     * Sets the value of the dividend yield that is being calculated
     * @param dividend the dividend yield for this stock
     */
    public void setDividend(double dividend) {
        this.dividendYield = dividend;
    }

    /**
     * The sector that the company does business in
     * @return stockSector the business sector
     */
    public String getSector() {
        return stockSector;
    }

    /**
     * Sets the business sector that the company operates within
     * @param name the business sector name
     */
    public void setSector(String name) {
        this.stockSector = name;
    }

    public Long getId() {
        return this.id;
    }

    /**
     * The custom toString for writing the stock info
     * @return String formatted string with stock info to be displayed
     */
    public String toString() {
        return String.format("[%s] %s. Sect: %s. Desc: %s. $%.2f/share.",
                ticker,name,stockSector,description,sharePrice);
    }
}
