package edu.sdccd.cisc191.common.entities;

public class StockList {
    private Stock[] stocks;
    private User owner;

    public static void main(String[] args) {
        System.out.println("Test");
    }

    public StockList(Stock[] conveyedStocks) {
        stocks = new Stock[conveyedStocks.length];
        for (int i=0;i<conveyedStocks.length;i++){
            stocks[i] = conveyedStocks[i];
        }
    }

    public String getTickers(){
        String outputString = "";
        for (int i=0;i<stocks.length;i++){
            outputString = outputString + stocks[i].ticker;
            if (i<stocks.length-1){
                outputString = outputString + ",";
            }
        }
        return outputString;
    }

    public String getAllStockInfo(){
        String outputString = "";
        String formatString = "%s (%s). $%.2f/share. Div yield $%.2f (%.2f%%). Sector: %s. Description: %s.";
        for (int i=0;i<stocks.length;i++){
            Stock thisStock = stocks[i];
            String thisLine = String.format(formatString,
                    thisStock.getName(), thisStock.getTicker(),
                    thisStock.getPrice(), thisStock.getDividend(),
                    thisStock.getDividend() / thisStock.getPrice() * 100,
                    thisStock.getSector(), thisStock.getDescription()
                    );
            outputString = outputString + thisLine;
            if (i<stocks.length-1){
                outputString = outputString + "\n";
            }
        }
        return outputString;
    }
}
