package edu.sdccd.cisc191.common.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

//Test class for StockList class
class StockListTest {

    //No Args Constructor testing.
    public StockList stockList = new StockList(); //No Args


    //Stock objects for testing.
    Stock stock = new Stock("ABCD", "Alphabet", "Testing stocks", "Testing", 12.34, 0.5);
    Stock stock2 = new Stock("FFFF", "FU University", "Get F'd!!!", "Testing", 33.45, 0.02);


    //Test that no-args stockList behaves properly by adding and removing stock objects
    //as well as test length/size of stockList.
    @Test
    void testNoArgsConstructor() {
        assertEquals(0, stockList.length());

        stockList.add(stock);
        assertEquals(1, stockList.length());

        stockList.add(stock2);
        assertEquals(2, stockList.length());

        stockList.delete(stock);
        assertEquals(1, stockList.length());

        stockList.delete(stock2);
        assertEquals(0, stockList.length());
    }

    @Test
    void testArgsConstructor() {
        LinkedList<Stock> stocksLinkedList = new LinkedList<>();
        stocksLinkedList.add(stock);
        stocksLinkedList.add(stock2);

        StockList stockList2 = new StockList(stocksLinkedList);

        assertEquals(2, stockList2.length());

        stockList2.delete(stock);
        assertEquals(1, stockList2.length());

        stockList2.add(stock);
        assertEquals(2, stockList2.length());

        stockList2.delete(stock);
        assertEquals(1, stockList2.length());

        stockList2.delete(stock2);
        assertEquals(0, stockList2.length());
    }
}