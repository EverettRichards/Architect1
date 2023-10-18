package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class StockCandleTest {


    @Test
    void getTicker() throws MalformedURLException, JsonProcessingException {
        StockCandle candle = new StockCandle("AAPL");
        assertEquals(candle.getTicker(),"AAPL");

        StockCandle candle2 = new StockCandle("MSFT");
        assertEquals(candle2.getTicker(),"MSFT");

        StockCandle candle3 = new StockCandle("NVDA");
        assertEquals(candle3.getTicker(),"NVDA");
    }

    @Test
    void invert2DArray() {
        double[][] myArray = {{1.0,2.0,3.0},{1.0,2.0,3.0},{1.0,2.0,3.0},{1.0,2.0,3.0}};
        double[][] expectedResult = {{1.0,1.0,1.0,1.0},{2.0,2.0,2.0,2.0},{3.0,3.0,3.0,3.0}};
        assertArrayEquals(StockCandle.invert2DArray(myArray),expectedResult);

        double[][] myArray2 = {{1,2},{3,4},{5,6},{7,8},{9,10}};
        double[][] expectedResult2 = {{1,3,5,7,9},{2,4,6,8,10}};
        assertArrayEquals(StockCandle.invert2DArray(myArray2),expectedResult2);
    }

//    @Test
//    void getStockInfo() throws MalformedURLException, JsonProcessingException {
//        StockCandle candle = new StockCandle("AAPL");
//        double[][] stockInfo = candle.getStockInfo();
//        // Verify that each bit is 3600 seconds (1 hour) apart
//        for (int i=0;i<5;i++){
//            assertEquals(stockInfo[i+1][4]-stockInfo[0][4],(i+1)*3600);
//        }
//    }

//    @Test
//    void testToString() throws MalformedURLException, JsonProcessingException {
//        StockCandle candle = new StockCandle("AAPL");
//        System.out.println(candle.toString());
//    }

//    @Test
//    void printConciseContents() throws MalformedURLException, JsonProcessingException {
//        StockCandle candle = new StockCandle("AAPL");
//        candle.printConciseContents();
//    }
}