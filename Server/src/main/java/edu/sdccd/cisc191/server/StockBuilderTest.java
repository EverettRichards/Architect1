package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

public class StockBuilderTest {

    @Test
    void test() throws MalformedURLException, JsonProcessingException {
        String[] myTickers = new String[]{"AAPL","MSFT","META","TSM","COST","K","TM","KO"};
        for (String ticker : myTickers) {
            Stock myStock = StockBuilder.newStock(ticker);
            String output = StockBuilder.stockToJson(myStock);
            System.out.println(output);
        }
    }
}