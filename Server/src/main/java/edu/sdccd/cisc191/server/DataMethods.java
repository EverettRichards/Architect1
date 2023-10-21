package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.FileNotFoundException;

public class DataMethods {

    public static final String stockDirectory = "server_storage\\stock_repository"; // Directory of where to store STOCK.json files
    public static final String stockCandleDirectory = "server_storage\\stock_candle_repository"; // Directory of where to store STOCKCANDLE.json files

    public static final String stockIdFileAddress = "server_storage\\stock_ids.json";

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
        JsonNode root = decodeJson(json);

        ObjectMapper map = new ObjectMapper();
        ObjectNode newNode = map.createObjectNode();

        double[][] initialData = new double[subKeys.length][];

        int index = 0;
        for (String key : subKeys) {
            JsonNode thisNode = root.get(key);
            double[] list = new double[thisNode.size()];
            for (int i = 0; i<list.length; i++){
                list[i] = thisNode.get(i).asDouble();
            }
            initialData[index] = list;
            index++;
        }

        double[][] formattedData = invert2DArray(initialData);

        newNode.put("ticker",ticker);
        newNode.put("last_updated",System.currentTimeMillis());
        newNode.put("json_version",ServerStockCandle.stockCandleJsonVersion);
        newNode.put("candle_count",formattedData.length);
        newNode.put("duration",duration);
        newNode.put("time1",time1);
        newNode.put("time2",time2);

        int i = 0;
        for (double[] row : formattedData) {
            ObjectMapper subMap = new ObjectMapper();
            ObjectNode subNode = subMap.createObjectNode();

            subNode.put("c",row[0]);
            subNode.put("h",row[1]);
            subNode.put("l",row[2]);
            subNode.put("o",row[3]);
            subNode.put("t",row[4]);
            subNode.put("v",row[5]);

            newNode.set(String.valueOf(i++),subNode);
        }

        return encodeJson(newNode);
    }

    public static void createFile(String path, String filename, String content) throws FileNotFoundException{
        createFile(path + "\\" + filename, content);
    }

    public static void createFile(String reference, String content) throws FileNotFoundException {
        PrintWriter result = new PrintWriter(reference);
        result.print(content);
        result.close();
    }

    public static String readFile(String path, String filename) throws IOException {
        return readFile(path + "\\" + filename);
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
}
