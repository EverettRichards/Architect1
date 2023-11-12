package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
This class is used for parsing and manipulating JSON data structures.

Specifically, it replaces some static methods previously found in the deprecated DataMethods.java,
but in a more memory-friendly manner.

A JsonObject simply contains a JsonNode and corresponding String representation as attributes.
 */
public class JsonObject {
    private String string;
    private JsonNode jsonNode;

    // Update string and jsonNode based on a provided JsonNode
    public void updateFromNode(JsonNode node) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        String output = map.writeValueAsString(node);
        string = output;
    }


    // Update string and jsonNode based on a provided String
    public void updateFromString(String input) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        JsonNode root = map.readTree(input);
        jsonNode = root;
    }

    // Constructor when provided a JsonNode
    public JsonObject(JsonNode node) throws JsonProcessingException {
        jsonNode = node;
        updateFromNode(node);
    }

    // Constructor when provided a String
    public JsonObject(String input) throws JsonProcessingException {
        string = input;
        updateFromString(input);
    }

    // Combines data from 2 JsonObjects to create one JsonObject with data needed
    // from 2 different FinnHub endpoints.
    public JsonObject mergeStockData(JsonObject other) throws JsonProcessingException {
        JsonNode jsonNode2 = other.getJsonNode();

        ObjectMapper map = new ObjectMapper();
        ObjectNode root3 = map.createObjectNode();

        root3.set("name",jsonNode.get("name"));
        root3.set("sector",jsonNode.get("finnhubIndustry"));
        root3.set("description",jsonNode.get("name"));
        root3.set("price",jsonNode2.get("c"));
        root3.put("last_updated",System.currentTimeMillis());
        root3.put("stock_version",ServerStock.stockJsonVersion);

        return new JsonObject(root3);
    }

    // Final array of the 6 subkeys used for indexing StockCandle API outputs.
    private final String[] subKeys = {"c","h","l","o","t","v"}; // Close, High, Low, Open ... Time, Volume

    // Adds extra metadata to JSON data for StockCandles.
    public JsonObject annotateForCandles(String ticker, String duration, long time1, long time2) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        ObjectNode newNode = map.createObjectNode();

        for (String key : subKeys) {
            newNode.set(key,jsonNode.get(key));
        }

        newNode.put("ticker",ticker);
        newNode.put("lastRefresh",System.currentTimeMillis());
        newNode.put("json_version",ServerStockCandle.stockCandleJsonVersion);
        newNode.put("duration",duration);
        newNode.put("time1",time1);
        newNode.put("time2",time2);
        return new JsonObject(newNode);
    }

    // Returns the String representation of the JsonObject
    public String getString(){
        return string;
    }

    // Returns the JsonNode representation of the JsonObject
    public JsonNode getJsonNode(){
        return jsonNode;
    }
}
