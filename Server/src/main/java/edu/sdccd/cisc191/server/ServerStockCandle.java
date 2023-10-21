package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    /*private void updateFromJsonNode(JsonNode root, Boolean updateTimeStamp){
        int count = root.get("candle_count").asInt();
        double[][] newStockInfo = new double[count][6];
        for (int i=0; i<count; i++){
            String index = String.valueOf(i);
            JsonNode subNode = root.get(index);
            double[] dataArray = new double[6];
            dataArray[0] = subNode.get("c").asDouble();
            dataArray[1] = subNode.get("h").asDouble();
            dataArray[2] = subNode.get("l").asDouble();
            dataArray[3] = subNode.get("o").asDouble();
            dataArray[4] = subNode.get("t").asDouble();
            dataArray[5] = subNode.get("v").asDouble();
            newStockInfo[i] = dataArray;
        }

        try {
            id = DataMethods.getStockId(getTicker());
        } catch (IOException e) {
            setId(0L);
        }

        duration = root.get("duration").asText();
        time1 = root.get("time1").asLong();
        time2 = root.get("time2").asLong();

        stockInfo = newStockInfo;

        if (updateTimeStamp) {
            // Update this so we know when the stock info needs to be renewed
            setLastRefresh(System.currentTimeMillis());
        } else {
            setLastRefresh(root.get("last_updated").asLong());
        }
    }*/


    // Call the API to get the latest information. Then, save the new data to the server.
    private void updateFromAPI() throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = getTicker();
        String finnhubResult = Finnhub.getCandleFromFinnhub(ticker,duration,time1,time2);
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
            System.out.println("Updating old-versioned stock candle.");
            updateFromAPI();
        } else if (System.currentTimeMillis() - root.get("lastRefresh").asLong() >= (1000L*getMaximumTimeBetweenRefreshes(duration))) {
            // Stock hasn't been updated recently. Don't use it.
            System.out.println("Updating outdated stock candle.");
            updateFromAPI();
        } else {
            // The stock is up-to-date. Load it WITHOUT an API call.
            System.out.println("Updating stock candle from file.");
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

        long[] timeRange = getTimeRange(duration);
        time1 = timeRange[0];
        time2 = timeRange[1];
        this.duration = duration;

        try {
            // Attempt to update the freshly instantiated stock
            updateFromBestMethod();
        } catch(Exception e) {
            // Failed!
        }
    }

    public ServerStockCandle(){

    }

    // Given a duration (i.e. day, week, year), find the start/end time stamps that surround the interval
    public static long[] getTimeRange(String duration){ // duration can be day, week, month, 6month, year

        ZoneId estZoneId = ZoneId.of("America/New_York");
        LocalDateTime now = LocalDateTime.now(estZoneId);
        DayOfWeek currentDayOfWeek = now.getDayOfWeek();
        LocalDateTime targetDateTime = now;

        if (duration.equals("day")) {
            if (currentDayOfWeek == DayOfWeek.SATURDAY || currentDayOfWeek == DayOfWeek.SUNDAY) {
                targetDateTime = now.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
            } else {
                targetDateTime = now;
            }
        } else if (duration.equals("week")) {
            if (currentDayOfWeek != DayOfWeek.MONDAY) {
                targetDateTime = now.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            } else {
                targetDateTime = now;
            }
        } else if (duration.equals("month")) {
            targetDateTime = targetDateTime.minusDays(30);
        } else if (duration.equals("6month")) {
            targetDateTime = targetDateTime.minusDays(183);
        } else if (duration.equals("year")) {
            targetDateTime = targetDateTime.minusDays(365);
        } else if (duration.equals("5year")) {
            targetDateTime = targetDateTime.minusDays(365*5);
        }

        long additionalTime = switch (duration) {
            case "day" -> 60*60*16; // 4:00PM
            case "week" -> 60*60*16 + 60*60*24*4; // Assume 5 days (M-F)
            case "month" -> 60*60*16 + 60*60*24*29; // Assume 30 days
            case "6month" -> 60*60*16 + 60*60*24*(182); // Assume 183 days
            case "year" -> 60*60*16 + 60*60*24*(364); // Assume 365 days
            case "5year" -> 60*60*16 + 60*60*24*(365*5); // Assume 365*5+1 days
            default -> 60*60*16;
        };

        targetDateTime = targetDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        long baseTime = targetDateTime.atZone(estZoneId).toInstant().toEpochMilli()/1000;
        long startTime = baseTime + (long)(60*60*9.5); // Opening time is always 9:30AM Eastern
        long endTime = baseTime + additionalTime;

        long[] stamps = new long[2];
        stamps[0] = startTime;
        stamps[1] = endTime;
        return stamps;
    }

    public static String getFrequency(String duration){
        String frequency = switch (duration) {
            case "day" -> "5";
            case "week" -> "15";
            case "month" -> "60";
            case "6month" -> "D";
            case "year" -> "D";
            case "5year" -> "W";
            default -> "5";
        };
        return frequency;
    }

    // Get the maximum allowed amount of time between refreshes.
    // Can implement a LINKED LIST here instead of case?
    public static int getMaximumTimeBetweenRefreshes(String duration){
        int maxTime = switch (duration) {
            case "day" -> 60; // 1 minute
            case "week" -> 60*5; // 5 minutes
            case "month" -> 60*60; // 1 hour
            case "6month" -> 60*60*24; // 1 day
            case "year" -> 60*60*24; // 1 day
            case "5year" -> 60*60*24; // 1 day
            default -> 60;
        };
        return maxTime;
    }

    public String toJson() throws JsonProcessingException, MalformedURLException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public String getFileName(){
        return String.format("%s;%s.json",ticker,duration);
    }

    // Creates a file, TICKER.json, containing the JSON form of a provided Stock object
    public void saveAsJsonFile() throws JsonProcessingException, FileNotFoundException, MalformedURLException {
        System.out.println("Saved a stock candle!!");
        String json = toJson();
        DataMethods.createFile(DataMethods.stockCandleDirectory,getFileName(),json);
    }
}
