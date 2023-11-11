package edu.sdccd.cisc191.common.errors;

public class BadTickerException extends Exception{
    public BadTickerException(String errorMessage) {
        super("Invalid Ticker: " + errorMessage);
    }
}