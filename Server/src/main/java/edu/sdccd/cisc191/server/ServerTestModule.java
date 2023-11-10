package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.server.errors.BadTickerException;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class ServerTestModule {
    String[] myTickers = new String[]{"TSLA","AAPL","KO","MSFT","COST"};//,"MSFT","META","TSM","COST","K","TM","KO"};
    /*@Test
    void testInstantiation() throws MalformedURLException, JsonProcessingException {
        for (String ticker : myTickers) {
            ServerStock myStock2 = new ServerStock(ticker);
            String output2 = myStock2.toJson();
            System.out.println(output2);

            ServerStockCandle myCandle = new ServerStockCandle(ticker,"week");
            String output3 = myCandle.toJson();
            System.out.println(output3);
        }
    }*/

    @Test
    void testJsonMethods() throws MalformedURLException, JsonProcessingException, BadTickerException {
        for (String ticker : myTickers) {
            ServerStock myStock = new ServerStock(ticker);
            ServerStockCandle candle1 = new ServerStockCandle(ticker, "day");
            String output = new ObjectMapper().writeValueAsString(candle1);
            System.out.println(output);
            ServerStockCandle candle = new ObjectMapper().readValue(output, ServerStockCandle.class);
        }
    }

    /*@Test
    void test2() throws MalformedURLException, JsonProcessingException {
        ServerStockCandle candle = null;
        System.out.println(candle.toJson());
    }*/
}