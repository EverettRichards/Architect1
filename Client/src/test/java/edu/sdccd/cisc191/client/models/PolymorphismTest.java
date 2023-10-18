package edu.sdccd.cisc191.client.models;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import edu.sdccd.cisc191.server.api.StockController;

class PolymorphismTest {

    @Test
    void testInterface() {//
        // Make sure that the StockController class is indeed an implementation of the DataFetcher interface
        StockController newObj = new StockController();
        assertTrue(newObj instanceof StockController);
        assertTrue(newObj instanceof DataFetcher);
    }

}