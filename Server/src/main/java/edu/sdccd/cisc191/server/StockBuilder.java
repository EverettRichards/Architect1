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
    public static void updateStockFromJsonNode(Stock stock, JsonNode root, Boolean updateTimeStamp){
        stock.setName(root.get("name").asText());
        stock.setSector(root.get("sector").asText());
        stock.setDescription(String.format("%s is a company in the %s sector.",stock.getName(),stock.getSector()));
        stock.setPrice(root.get("price").asDouble());
        stock.setLastRefresh(root.get("last_updated").asLong());

        if (updateTimeStamp) {
            // Update this so we know when the stock info needs to be renewed
            stock.setLastRefresh(System.currentTimeMillis());
        }
    }

    public static void updateStockFromAPI(Stock stock) throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = stock.getTicker();
        String finnhubResult = FinnhubNetworking.getJsonFromFinnhub(ticker);
        JsonNode root = DataMethods.decodeJson(finnhubResult);
        updateStockFromJsonNode(stock,root,true);
        saveStock(stock);
    }

    public static void updateStockFromFile(Stock stock) throws IOException {
        String contents = DataMethods.readFile("data",stock.getTicker()+".json");
        JsonNode root = DataMethods.decodeJson(contents);
        updateStockFromJsonNode(stock,root,false);
        if (System.currentTimeMillis() - stock.getLastRefresh() >= (1000L*FinnhubNetworking.secondsBeforeRefreshNeeded)) {
            updateStockFromAPI(stock);
        }
    }

    public static void updateStock(Stock stock) throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = stock.getTicker();
        JsonNode root;

        try {
            updateStockFromFile(stock);
        } catch (IOException e) {
            System.out.println("File not found. Find from API.");
            updateStockFromAPI(stock);
        }


    }

    public static void checkForStockUpdates(Stock stock) throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        long lastRefresh = stock.getLastRefresh();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefresh > FinnhubNetworking.secondsBeforeRefreshNeeded*1000){
            System.out.println("This stock has not been updated for too long. Force update.");
            updateStock(stock);
        }
    }

    public static Stock newStock(String ticker) throws MalformedURLException, JsonProcessingException {
        Stock stock = new Stock(ticker);
        try {
            updateStock(stock);
        } catch(Exception e) {
            // Failed!
        }
        return stock;
    }

    public static String stockToJson(Stock stock) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        ObjectNode parent = map.createObjectNode();

        parent.put("name",stock.getName());
        parent.put("sector",stock.getSector());
        parent.put("description",stock.getDescription());
        parent.put("price",stock.getPrice());
        parent.put("last_updated",stock.getLastRefresh());

        return DataMethods.encodeJson(parent);
    }

    public static void saveStock(Stock stock) throws JsonProcessingException, FileNotFoundException {
        System.out.println("Saved a stock!!");
        String json = stockToJson(stock);
        DataMethods.createFile("data",stock.getTicker()+".json",json);
    }
}
