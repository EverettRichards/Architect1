package edu.sdccd.cisc191.common.errors;

// Exception to be thrown when an invalid ticker is requested from the server (via user input)
public class BadTickerException extends Exception{
    public BadTickerException(String errorMessage) {
        super("Invalid Ticker: " + errorMessage);
    }
}