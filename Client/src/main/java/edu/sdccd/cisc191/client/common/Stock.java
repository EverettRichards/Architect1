package edu.sdccd.cisc191.client.common;

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

    public static long lastId = 0L;

    /**
     * Default constructor for the Stock class
     */
    public Stock () {
    }

    public Stock(long id, String ticker, String newName, String description,
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

    public void Update() throws JsonProcessingException, MalformedURLException {
        // Get the JSON data from Finnhub with basic company info such as name, sector, etc.
        String jsonInput = Requests.get("https://finnhub.io/api/v1/stock/profile2?symbol="
                + ticker + "&token=" + Requests.token);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonInput);

        // Update core attributes accordingly
        name = rootNode.get("name").asText();
        description = "A company called "+name;
        stockSector = rootNode.get("finnhubIndustry").asText();

        // Get the JSON data with FINANCIAL info such as price
        String jsonInput2 = Requests.get("https://finnhub.io/api/v1/quote?symbol="
                + ticker + "&token=" + Requests.token);
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode rootNode2 = mapper2.readTree(jsonInput2);

        // Update core attributes accordingly
        sharePrice = rootNode2.get("c").asDouble();
    }

    public Stock(String newTicker) throws MalformedURLException, JsonProcessingException {
        // Internally assign the new Ticker value
        ticker = newTicker;
        // Increment and set the stock's unique ID as a long
        id = ++lastId;
        // Get live, up-to-date information about the stock
        Update();
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
