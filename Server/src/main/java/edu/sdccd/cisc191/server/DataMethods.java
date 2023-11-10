package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.sdccd.cisc191.common.entities.DataFetcher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;

public class DataMethods {

    public static final String mainDirectory = "Server/server_storage";
    public static final String stockDirectory = mainDirectory + "/stock_repository"; // Directory of where to store STOCK.json files
    public static final String stockCandleDirectory = mainDirectory + "/stock_candle_repository"; // Directory of where to store STOCKCANDLE.json files
    public static final String stockIdFileAddress = mainDirectory + "/stock_ids.json";
    public static final String defaultTickersFileAddress = "Common/src/main/static_data/default_tickers.txt";
    public static final String allTickersFileAddress = "Common/src/main/static_data/all_tickers.txt";

    public static void instantiateStockIdFile() {
        try {
            FileWriter writer = new FileWriter(stockIdFileAddress);
            writer.write("" +
                    "" +
                    "{\"last_id\":1,\"AAPL\":1}");
            writer.close();
        } catch (Exception e) {

        }
    }

    public static double[][] invert2DArray(double[][] inputArray){
        int rows = inputArray.length;
        int columns = inputArray[0].length;

        double[][] newArray = new double[columns][rows];
        for (int i=0;i<rows;i++){
            for (int j=0;j<columns;j++){
                newArray[j][i] = inputArray[i][j];
            }
        }

        return newArray;
    }

    // JSON String to Object
    public static JsonNode decodeJson(String rawData) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        JsonNode root = map.readTree(rawData);
        return root;
    }

    // JSON Object to String
    public static String encodeJson(JsonNode root) throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        String output = map.writeValueAsString(root);
        return output;
    }

    // mergeJson returns a single JSON String with elements from both API calls,
    // used for internal methods and class interactions
    public static String mergeStockJson(String json1, String json2) throws JsonProcessingException {
        JsonNode root1 = decodeJson(json1);
        JsonNode root2 = decodeJson(json2);

        ObjectMapper map = new ObjectMapper();
        ObjectNode root3 = map.createObjectNode();

        root3.set("name",root1.get("name"));
        root3.set("sector",root1.get("finnhubIndustry"));
        root3.set("description",root1.get("name"));
        root3.set("price",root2.get("c"));
        root3.put("last_updated",System.currentTimeMillis());
        root3.put("stock_version",ServerStock.stockJsonVersion);

        return encodeJson(root3);
    }

    public static String mergeJson(String json1, String json2, String[] inKeys1, String[] inKeys2) throws JsonProcessingException {
        return mergeJson(json1,json2,inKeys1,inKeys1,inKeys2,inKeys2);
    }

    public static String mergeJson(String json1, String json2, String[] inKeys1, String[] inKeys2, String[] outKeys1, String[] outKeys2) throws JsonProcessingException {
        JsonNode root1 = decodeJson(json1);
        JsonNode root2 = decodeJson(json2);

        ObjectMapper map = new ObjectMapper();
        ObjectNode root3 = map.createObjectNode();

        for (int i = 0; i < inKeys1.length; i++){
            root3.set(outKeys1[i],root1.get(inKeys1[i]));
        }

        for (int i = 0; i < inKeys2.length; i++){
            root3.set(outKeys2[i],root2.get(inKeys2[i]));
        }

        return encodeJson(root3);
    }


    private static final String[] subKeys = {"c","h","l","o","t","v"}; // Close, High, Low, Open ... Time, Volume
    public static String annotateCandles(String json, String ticker, String duration, long time1, long time2) throws JsonProcessingException {
        System.out.println(json);
        JsonNode root = decodeJson(json);

        ObjectMapper map = new ObjectMapper();
        ObjectNode newNode = map.createObjectNode();

        for (String key : subKeys) {
            newNode.set(key,root.get(key));
        }

        newNode.put("ticker",ticker);
        newNode.put("lastRefresh",System.currentTimeMillis());
        newNode.put("json_version",ServerStockCandle.stockCandleJsonVersion);
        // I got an error when trying to set the count/length
        newNode.put("duration",duration);
        newNode.put("time1",time1);
        newNode.put("time2",time2);
        return encodeJson(newNode);
    }

    public static void createFile(String path, String filename, String content) throws FileNotFoundException{
        createFile(path + "/" + filename, content);
    }

    public static void createFile(String reference, String content) throws FileNotFoundException {
        PrintWriter result = new PrintWriter(reference);
        result.print(content);
        result.close();
    }

    public static String readFile(String path, String filename) throws IOException {
        return readFile(path + "/" + filename);
    }

    public static String readFile(String reference) throws IOException {
        FileInputStream stream = new FileInputStream(reference);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String content = reader.readLine();

        reader.close();
        stream.close();

        return content;
    }

    public static long getStockId(String ticker) throws IOException {
        String idJson = readFile(stockIdFileAddress);
        long id = 0;
        try {
            JsonNode root = decodeJson(idJson);
            id = root.get(ticker).asLong();
        } catch (Exception e) {
            id = assignStockId(ticker);
        }
        return id;
    }

    public static long assignStockId(String ticker) throws IOException {
        String content = readFile(stockIdFileAddress);
        ObjectNode root = (ObjectNode)decodeJson(content);

        long lastId = root.get("last_id").asLong();
        lastId++;
        root.put(ticker,lastId);
        root.put("last_id",lastId);

        String output = encodeJson(root);
        createFile(stockIdFileAddress,output);

        return lastId;
    }

    private static String[] defaultTickers = new String[0];

    public static String[] getDefaultTickers(){
        if (defaultTickers.length == 0) {
            try {
                defaultTickers = Files.lines(Paths.get(DataMethods.defaultTickersFileAddress))
                        .collect(Collectors.toList())
                        .toArray(new String[0]);
            } catch (IOException e) {
                defaultTickers = new String[]{"AAPL"};
            }
        }
        return defaultTickers;
    }

    /*public static String[] parseTickerJson(String input) throws JsonProcessingException {
        JsonNode node = decodeJson(input);
        String[] result = new String[node.size()];
        for (int i = 0; i < node.size(); i++){
            result[i] = node.get(i).get("symbol").asText();
        }
        return result;
    }*/

    private static String[] allTickers = new String[0];

    public static String[] getAllTickers(){
        if (allTickers.length == 0) {
            try {
                allTickers = Files.lines(Paths.get(DataMethods.allTickersFileAddress))
                        .collect(Collectors.toList())
                        .toArray(new String[0]);
            } catch (IOException e) {
                allTickers = new String[]{"AAPL"};
            }
        }
        return allTickers;
    }

    public static boolean isValidTicker(String ticker){
        if (allTickers.length==0){
            getAllTickers();
        }

        for (String thisTicker : allTickers){
            if (thisTicker.equals(ticker)){
                return true;
            }
        }

        return false;
    }
}
