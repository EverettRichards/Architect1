package edu.sdccd.cisc191.client.models;

import edu.sdccd.cisc191.client.controllers.StockController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.server.StockTrader;

class PolymorphismTest {

    @Test
    void testInterface() {//
        // Make sure that the StockController class is indeed an implementation of the DataFetcher interface
        StockController newObj = new StockController();
        assertTrue(newObj instanceof StockController);
        assertTrue(newObj instanceof DataFetcher);
    }

    @Test
    void testAbstract() {
        StockTrader trader = new StockTrader("Name",1);
        assertTrue(trader instanceof StockTrader);
        assertTrue(trader instanceof User);
    }


}