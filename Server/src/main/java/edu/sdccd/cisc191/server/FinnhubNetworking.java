package edu.sdccd.cisc191.server;

//import edu.sdccd.cisc191.common.entities.Stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.common.entities.Requests;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;

import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class FinnhubNetworking {

    private long secondsBeforeRefreshNeeded = 60; // number of seconds before a cached stock will be forced to refresh

    public void UpdateStock(Stock stock) throws JsonProcessingException, MalformedURLException {
        // Get the JSON data from Finnhub with basic company info such as name, sector, etc.
        String jsonInput = Requests.get("https://finnhub.io/api/v1/stock/profile2?symbol="
                + stock.getTicker() + "&token=" + Requests.token);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonInput);

        stock.setLastRefresh(System.currentTimeMillis());

        // Update core attributes accordingly
        stock.setName(rootNode.get("name").asText());
        stock.setSector(rootNode.get("finnhubIndustry").asText());
        stock.setDescription(String.format("%s is a company in the %s sector.",stock.getName(),stock.getSector()));

        // Get the JSON data with FINANCIAL info such as price
        String jsonInput2 = Requests.get("https://finnhub.io/api/v1/quote?symbol="
                + stock.getTicker() + "&token=" + Requests.token);
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode rootNode2 = mapper2.readTree(jsonInput2);

        // Update core attributes accordingly
        stock.setPrice(rootNode2.get("c").asDouble());
    }

    // Update the stock only if necessary
    public void CheckForStockUpdates(Stock stock) throws JsonProcessingException, MalformedURLException {
        long lastRefresh = stock.getLastRefresh();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefresh > secondsBeforeRefreshNeeded*1000){
            System.out.println("This stock has not been updated for too long. Force update.");
            UpdateStock(stock);
            // Soon, will add functionality to update the persistent form of the stock
        }
    }

    // getTimeRange returns the start/end time stamps used when searching for a given stock candle chart
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

    private static final String token = "bsq5ig8fkcbcavsjbrrg";

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

    private static final String[] subKeys = {"c","h","l","o","t","v"};

    public static StockCandle newStockCandle(String newTicker, String resolution, long time1, long time2) throws MalformedURLException, JsonProcessingException {
        StockCandle candle = new StockCandle();
        UpdateCandle(candle,newTicker,resolution,time1,time2);
        return candle;
    }

    public static void UpdateCandle(StockCandle candle, String newTicker, String duration) throws MalformedURLException, JsonProcessingException {
        long[] timeRange = getTimeRange(duration);
        String frequency = getFrequency(duration);
        UpdateCandle(candle, newTicker,frequency, timeRange[0],timeRange[1]);
    }
    public static void UpdateCandle(StockCandle candle, String newTicker) throws MalformedURLException, JsonProcessingException {
        UpdateCandle(candle, newTicker,"5year");
    }

    private static void UpdateCandle(StockCandle candle, String newTicker, String resolution, long time1, long time2)
            throws JsonProcessingException, MalformedURLException {
        String URL = "https://finnhub.io/api/v1/stock/candle?symbol="+newTicker
                +"&resolution="+resolution
                +"&from="+time1+"&to="+time2
                +"&token="+token;
        String jsonInput = Requests.get(URL);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonInput);

        double[][] result = new double[subKeys.length][];

        int keyIndex = 0;

        for (String subKey : subKeys) {

            JsonNode tNode = rootNode.get(subKey);
            result[keyIndex] = new double[tNode.size()];

            for (int i = 0; i < tNode.size(); i++) {
                result[keyIndex][i] = tNode.get(i).asDouble();
            }

            keyIndex++;

        }

        candle.setStockInfo(invert2DArray(result));
    }
}
