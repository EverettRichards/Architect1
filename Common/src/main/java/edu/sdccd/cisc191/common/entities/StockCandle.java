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

    private static double[][] stockInfo;

    public void setTicker(String txt){
        ticker = txt;
    }

    public String getTicker(){
        return ticker;
    }

    public StockCandle(String newTicker, double[][] array){
        setTicker(newTicker);
        setStockInfo(array);
    }

    public StockCandle(){
        // Blank constructor
    }

    public double[][] getStockInfo(){
        return stockInfo;
    }

    public void setStockInfo(double[][] array){
        stockInfo = array;
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
