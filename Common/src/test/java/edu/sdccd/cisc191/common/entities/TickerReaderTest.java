package edu.sdccd.cisc191.common.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TickerReaderTest {

    @Test
    void TickerReader() {
        TickerReader reader = new TickerReader();
        String[] def = reader.getDefaultTickers();
        String[] all = reader.getAllTickers();
        assertTrue(def.length>5);
        assertTrue(all.length>1000);
    }

}