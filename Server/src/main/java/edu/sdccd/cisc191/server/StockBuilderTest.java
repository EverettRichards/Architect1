package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class StockBuilderTest {
    String[] myTickers = new String[]{"AAPL"};//,"MSFT","META","TSM","COST","K","TM","KO"};
    @Test
    void test() throws MalformedURLException, JsonProcessingException {
        for (String ticker : myTickers) {
            Stock myStock = StockBuilder.newStock(ticker);
            String output = StockBuilder.stockToJson(myStock);
            ServerStock myStock2 = new ServerStock(ticker);
            String output2 = myStock2.toJson();
            assertEquals(output, output2);
            System.out.println(output);
        }
    }
}