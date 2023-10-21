package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class StockBuilderTest {
    String[] myTickers = new String[]{"AAPL","COST","KO"};//,"MSFT","META","TSM","COST","K","TM","KO"};
    @Test
    void test() throws MalformedURLException, JsonProcessingException {
        for (String ticker : myTickers) {
            ServerStock myStock2 = new ServerStock(ticker);
            String output2 = myStock2.toJson();
            System.out.println(output2);

            ServerStockCandle myCandle = new ServerStockCandle(ticker,"week");
            String output3 = myCandle.toJson();
            System.out.println(output3);
        }
    }

    /*@Test
    void test2() throws MalformedURLException, JsonProcessingException {
        ServerStockCandle candle = StockCandleBuilder.newStockCandle("AAPL");
        System.out.println(candle.toJson());
    }*/
}