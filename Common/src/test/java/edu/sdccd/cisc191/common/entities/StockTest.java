package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @Test
    void testGetTicker() throws MalformedURLException, JsonProcessingException {
        Stock theStock = new Stock("AAPL");
        String ticker = theStock.getTicker();
        assertEquals(ticker,"AAPL");

        Stock theStock2 = new Stock("MSFT");
        String ticker2 = theStock2.getTicker();
        assertEquals(ticker2,"MSFT");
    }
}