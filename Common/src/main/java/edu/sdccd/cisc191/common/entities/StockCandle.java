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

    public static long getLastMktOpenTime(){
        ZoneId estZoneId = ZoneId.of("America/New_York");
        LocalDateTime now = LocalDateTime.now(estZoneId);
        DayOfWeek currentDayOfWeek = now.getDayOfWeek();
        LocalDateTime targetDateTime;

        if (currentDayOfWeek == DayOfWeek.SATURDAY || currentDayOfWeek == DayOfWeek.SUNDAY) {
            targetDateTime = now.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        } else {
            targetDateTime = now;
        }

        targetDateTime = targetDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        long timestamp = targetDateTime.atZone(estZoneId).toInstant().toEpochMilli()/1000;
        return timestamp;
    }
    public static void refreshData(String newTicker) throws MalformedURLException, JsonProcessingException {
        long longTime = getLastMktOpenTime();
        refreshData(newTicker,"5", (long) (longTime+(60*60*9.5)),(longTime+(60*60*16)));
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
