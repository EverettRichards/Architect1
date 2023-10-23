package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class StockBuilderTest {
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
    void testJsonMethods() throws MalformedURLException, JsonProcessingException {
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
        ServerStockCandle candle = StockCandleBuilder.newStockCandle("AAPL");
        System.out.println(candle.toJson());
    }*/
}