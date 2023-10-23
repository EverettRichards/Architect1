package edu.sdccd.cisc191.client.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DefaultStocksFileIOTest {
    private final DefaultStocksFileIO defaultStocks = new DefaultStocksFileIO();

    @Test
    void testReadAndUpdateDefaultStocks() {
        assertEquals(0, defaultStocks.readAndUpdateDefaultStocks(DefaultStocksFileIO.defaultStocks));
        System.out.println(defaultStocks.getDefaultStocks());
        assertEquals(-1, defaultStocks.readAndUpdateDefaultStocks("stocks.txt"));
    }
}