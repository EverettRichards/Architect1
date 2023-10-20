package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class StockBuilderTest {
    String[] myTickers = new String[]{"AAPL","COST","NVDA"};//,"MSFT","META","TSM","COST","K","TM","KO"};
    @Test
    void test() throws MalformedURLException, JsonProcessingException {
        for (String ticker : myTickers) {
            /*Stock myStock = StockBuilder.newStock(ticker);
            String output = StockBuilder.stockToJson(myStock);

            ServerStock myStock2 = new ServerStock(ticker);
            String output2 = myStock2.toJson();
            assertEquals(output.length(), output2.length());
            System.out.println(output);*/

            ServerStockCandle myCandle = new ServerStockCandle("AAPL","day");
            //String output3 = myCandle.toJson();
            //System.out.println(output3);
        }
    }

    /*@Test
    void test2() throws MalformedURLException, JsonProcessingException {
        ServerStockCandle candle = StockCandleBuilder.newStockCandle("AAPL");
        System.out.println(candle.toJson());
    }*/
}