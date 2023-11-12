package edu.sdccd.cisc191.common.entities;

import edu.sdccd.cisc191.common.errors.BadTickerException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TickerTest {
    @Test
    void testTicker() {
        boolean[] results = new boolean[3];

        try {
            Ticker ticker = new Ticker("AAPL");
            results[0] = true;
        } catch (BadTickerException e){
            results[0] = false;
        }

        try {
            Ticker ticker = new Ticker("AAAA FAKE TICKER");
            results[1] = false;
        } catch (BadTickerException e){
            results[1] = true;
        }

        try {
            Ticker ticker = new Ticker("AMZN");
            String tick = ticker.getTicker();
            results[2] = tick.equals("AMZN");
        } catch (BadTickerException e){
            results[2] = false;
        }

        for (boolean b : results){
            //System.out.println(b);
            assertTrue(b);
        }
    }
}