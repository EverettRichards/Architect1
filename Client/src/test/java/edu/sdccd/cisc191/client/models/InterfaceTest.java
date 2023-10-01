package edu.sdccd.cisc191.client.models;

import edu.sdccd.cisc191.client.controllers.StockController;
import edu.sdccd.cisc191.common.entities.Stock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class InterfaceTest {

    @Test
    void testInterface() {//
        // Make sure that the StockController class is indeed an implementation of the DataFetcher interface
        StockController newObj = new StockController();
        assertTrue(newObj instanceof DataFetcher);
    }
}