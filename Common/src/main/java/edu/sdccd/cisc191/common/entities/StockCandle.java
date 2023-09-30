package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class StockCandle {
    private String ticker;

    private static final String[] subKeys = {"c","h","l","o","t","v"};

    private static double[][] stockInfo;

    public void setTicker(String txt){
        ticker = txt;
    }

    public String getTicker(){
        return ticker;
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

    public static void refreshData(String newTicker, String duration) throws MalformedURLException, JsonProcessingException {
        long[] timeRange = getTimeRange(duration);
        String frequency = getFrequency(duration);
        refreshData(newTicker,frequency, timeRange[0],timeRange[1]);
    }
    public static void refreshData(String newTicker) throws MalformedURLException, JsonProcessingException {
        refreshData(newTicker,"5year");
    }

    private static void refreshData(String newTicker, String resolution, long time1, long time2)
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

        stockInfo = invert2DArray(result);

        //System.out.println(stockInfo[0][0]);
    }

    /*public static void main(String[] args) throws JsonProcessingException {
        refreshData(candleExample);
    }*/

    public StockCandle(String newTicker) throws JsonProcessingException, MalformedURLException {
        refreshData(newTicker);
        setTicker(newTicker);
    }

    public StockCandle(String newTicker, String resolution, long time1, long time2) throws MalformedURLException, JsonProcessingException {
        refreshData(newTicker,resolution,time1,time2);
        setTicker(newTicker);
    }

    public double[][] getStockInfo(){
        return stockInfo;
    }
    
    public String toString(){
        String outputString = "";
        for (double[] row : stockInfo){
            outputString += String.format("@t=%.0f, %s low=%.2f, high=%.2f, open=%.2f, close=%.2f.",row[4],ticker,row[2],row[1],row[3],row[0]);
            outputString += "\n";
        }
        return outputString;
    }

    public void printConciseContents(){
        System.out.println(ticker+":");
        for (double[] row : stockInfo){
            String outputString = "{";
            for (double item : row){
                outputString += item;
                if (item != row[row.length-1]){
                    outputString+=", ";
                }
            }
            outputString+="}";
            if (row != stockInfo[stockInfo.length-1]) {
                outputString += ", ";
            }
            System.out.println(outputString);
        }
    }
}
