package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.common.entities.StockCandle;
import edu.sdccd.cisc191.server.concurrency.FinnhubTask;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.io.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerStockCandle extends StockCandle {
    public static final int stockCandleJsonVersion = 11; // The internal version of the Stock.java JSON files.
    // If you make any changes to the expected/actual format of JSON stock files, please add 1 to this value.

    public int jsonVersion; // JSON version for this specific object

    // Update the attributes of the Stock using a JSON data structure. Used for instantiation
    // once valid JSON is found either from the API or the server.

    private void updateFromJsonNode(JsonNode root, Boolean updateTimeStamp) throws JsonProcessingException {
        String json = DataMethods.encodeJson(root);
        ServerStockCandle newCandle = new ObjectMapper().readValue(json,ServerStockCandle.class);

        this.c = newCandle.c;
        this.h = newCandle.h;
        this.l = newCandle.l;
        this.o = newCandle.o;
        this.t = newCandle.t;
        this.v = newCandle.v;

        this.ticker = newCandle.getTicker();
        this.id = newCandle.getId();
        this.lastRefreshTime = newCandle.getLastRefresh();
        this.duration = newCandle.duration;
        this.time1 = newCandle.time1;
        this.time2 = newCandle.time2;
        this.jsonVersion = stockCandleJsonVersion;

        try {
            this.id = DataMethods.getStockId(getTicker());
        } catch (IOException e) {
            setId(0L);
        }
    }


    // Call the API to get the latest information. Then, save the new data to the server.
    private void updateFromAPI() throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = getTicker();
        String finnhubResult = FinnhubNetworking.getCandleFromFinnhub(ticker,duration,time1,time2);
        JsonNode root = DataMethods.decodeJson(finnhubResult);
        // ERROR HAPPENS here (below)
        updateFromJsonNode(root,true);
        saveAsJsonFile();
    }

    // Opens the corresponding JSON file to see if it's good for using.
    private void updateFromFile() throws IOException {
        String contents = DataMethods.readFile(DataMethods.stockCandleDirectory,getFileName());
        JsonNode root = DataMethods.decodeJson(contents);

        if (root.get("jsonVersion").asInt() != stockCandleJsonVersion) {
            // Stock has an old version. Don't use it.
            // System.out.println("Updating old-versioned stock candle.");
            updateFromAPI();
        } else if (System.currentTimeMillis() - root.get("lastRefresh").asLong() >= (1000L*TimeMethods.getMaximumTimeBetweenRefreshes(duration))) {
            // Stock hasn't been updated recently. Don't use it.
            // System.out.println("Updating outdated stock candle.");
            updateFromAPI();
        } else {
            // The stock is up-to-date. Load it WITHOUT an API call.
            // System.out.println("Updating stock candle from file.");
            updateFromJsonNode(root,false);
        }
    }

    // Updates an existing Stock object using the best method available (stored file or API).
    // Also used to initialize a brand new Stock object.
    public void updateFromBestMethod() throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        try {
            // Attempt to instantiate the Stock using data from a JSON file on the server.
            updateFromFile();
        } catch (IOException e) {
            // If there was no file to open, use the API and then create a file.
            System.out.println("Candle file not found (Candle). Find from API.");
            updateFromAPI();
        }
    }

    // Creates a new stock using the best data. Like a constructor, but defined outside of Stock.java itself.
    // Will use an existing file OR the FinnHub API, depending on the availability and recency of a relevant file.
    public ServerStockCandle(String ticker, String duration) throws MalformedURLException, JsonProcessingException {
        setTicker(ticker);

        long[] timeRange = TimeMethods.getTimeRange(duration);
        time1 = timeRange[0];
        time2 = timeRange[1];
        System.out.println(time1 + " " + time2);
        this.duration = duration;

        try {
            // Attempt to update the freshly instantiated stock
            updateFromBestMethod();
        } catch(Exception e) {
            // Failed!
        }
    }

    public ServerStockCandle(String ticker, String duration, long time1, long time2){
        setTicker(ticker);

        this.duration = duration;

        try {
            // Attempt to update the freshly instantiated stock
            updateFromBestMethod();
        } catch(Exception e) {
            // Failed!
        }
    }

    public static ServerStockCandle fetchCandle(FinnhubTask task){
        return new ServerStockCandle(task.getTicker(),task.getDuration(),task.getStartTime().toEpochMilli(),task.getEndTime().toEpochMilli());
    }

    public ServerStockCandle(){

    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    private String getFileName(){
        return String.format("%s;%s.json",ticker,duration);
    }

    // Creates a file, TICKER.json, containing the JSON form of a provided Stock object
    public void saveAsJsonFile() throws JsonProcessingException, FileNotFoundException {
        // System.out.println("Saved a stock candle!!");
        String json = toJson();
        DataMethods.createFile(DataMethods.stockCandleDirectory,getFileName(),json);
    }
}
