package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonObject {
    private String string;
    private JsonNode jsonNode;

    public void updateFromNode(JsonNode node) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        String output = map.writeValueAsString(node);
        string = output;
    }

    public void updateFromString(String input) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        JsonNode root = map.readTree(input);
        jsonNode = root;
    }

    public JsonObject(JsonNode node) throws JsonProcessingException {
        jsonNode = node;
        updateFromNode(node);
    }

    public JsonObject(String input) throws JsonProcessingException {
        string = input;
        updateFromString(input);
    }

    public void addObject(String key, String value){
        ObjectNode node = (ObjectNode) jsonNode;
        node.put(key,value);
        jsonNode = node;
    }

    public void addObject(String key, long value){
        ObjectNode node = (ObjectNode) jsonNode;
        node.put(key,value);
        jsonNode = node;
    }

    public void addObject(String key, double value){
        ObjectNode node = (ObjectNode) jsonNode;
        node.put(key,value);
        jsonNode = node;
    }

    public JsonObject merge(JsonObject other) throws JsonProcessingException {
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

    private static final String[] subKeys = {"c","h","l","o","t","v"}; // Close, High, Low, Open ... Time, Volume
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

    public String getString(){
        return string;
    }

    public JsonNode getJsonNode(){
        return jsonNode;
    }
}
