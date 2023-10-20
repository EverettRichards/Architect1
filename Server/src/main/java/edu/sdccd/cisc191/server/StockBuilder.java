package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.sdccd.cisc191.common.entities.Stock;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StockBuilder {
    public static final int stockJsonVersion = 1;

    private static void updateStockFromJsonNode(Stock stock, JsonNode root, Boolean updateTimeStamp){
        stock.setName(root.get("name").asText());
        stock.setSector(root.get("sector").asText());
        stock.setDescription(String.format("%s is a company in the %s sector.",stock.getName(),stock.getSector()));
        stock.setPrice(root.get("price").asDouble());
        stock.setLastRefresh(root.get("last_updated").asLong());
        stock.setDividend(0);

        if (updateTimeStamp) {
            // Update this so we know when the stock info needs to be renewed
            stock.setLastRefresh(System.currentTimeMillis());
        }
    }

    private static void updateStockFromAPI(Stock stock) throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = stock.getTicker();
        String finnhubResult = FinnhubNetworking.getJsonFromFinnhub(ticker);
        JsonNode root = DataMethods.decodeJson(finnhubResult);
        updateStockFromJsonNode(stock,root,true);
        saveStock(stock);
    }


    private static void updateStockFromFile(Stock stock) throws IOException {
        String contents = DataMethods.readFile("data",stock.getTicker()+".json");
        JsonNode root = DataMethods.decodeJson(contents);

        if (root.get("json_version").asInt() != stockJsonVersion) {
            // Stock has an old version. Don't use it.
            System.out.println("Updating old-versioned stock.");
            updateStockFromAPI(stock);
        } else if (System.currentTimeMillis() - root.get("last_updated").asLong() >= (1000L*FinnhubNetworking.secondsBeforeRefreshNeeded)) {
            // Stock hasn't been updated recently. Don't use it.
            System.out.println("Updating outdated stock.");
            updateStockFromAPI(stock);
        } else {
            // The stock is up-to-date. Load it WITHOUT an API call.
            updateStockFromJsonNode(stock,root,false);
        }
    }

    // Updates an existing Stock object using the best method available (stored file or API).
    // Also used to initialize a brand new Stock object.
    public static void updateStock(Stock stock) throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = stock.getTicker();
        JsonNode root;

        try {
            // Attempt to instantiate the Stock using data from a JSON file on the server.
            updateStockFromFile(stock);
        } catch (IOException e) {
            // If there was no file to open, use the API and then create a file.
            System.out.println("File not found. Find from API.");
            updateStockFromAPI(stock);
        }


    }

    // Creates a new stock using the best data. Like a constructor, but defined outside of Stock.java itself.
    // Will use an existing file OR the FinnHub API, depending on the availability and recency of a relevant file.
    public static Stock newStock(String ticker) throws MalformedURLException, JsonProcessingException {
        Stock stock = new Stock(ticker);
        try {
            updateStock(stock);
        } catch(Exception e) {
            // Failed!
        }
        return stock;
    }

    // Returns a String containing the attributes of a Stock object in JSON format.
    public static String stockToJson(Stock stock) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        ObjectNode parent = map.createObjectNode();

        // Put the stock's attributes into the JSON object
        parent.put("ticker",stock.getTicker());
        parent.put("name",stock.getName());
        parent.put("sector",stock.getSector());
        parent.put("description",stock.getDescription());
        parent.put("price",stock.getPrice());
        parent.put("dividend",0);
        parent.put("last_updated",stock.getLastRefresh());
        parent.put("json_version",stockJsonVersion);

        // Return the JSON-formatted String
        return DataMethods.encodeJson(parent);
    }

    // Creates a file, TICKER.json, containing the JSON form of a provided Stock object
    public static void saveStock(Stock stock) throws JsonProcessingException, FileNotFoundException {
        System.out.println("Saved a stock!!");
        String json = stockToJson(stock);
        DataMethods.createFile("data",stock.getTicker()+".json",json);
    }
}
