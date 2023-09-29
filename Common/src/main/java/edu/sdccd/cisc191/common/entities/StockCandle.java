package edu.sdccd.cisc191.common.entities;

public class StockCandle {
    private String ticker;
    private double[][] stockInfo;

    public void setTicker(String txt){
        ticker = txt;
    }

    public String getTicker(){
        return ticker;
    }

    public StockCandle(String jsonInput){
        // parse jsonInput
    }
}
