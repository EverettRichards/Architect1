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

    public Object[][] getStocksAs2DArray(){
        Object[][] mainArray = new Object[stocks.length][6];
        int i = 0;
        for (Stock thisStock : stocks) {
            mainArray[i][0] = thisStock.getTicker();
            mainArray[i][1] = thisStock.getName();
            mainArray[i][2] = thisStock.getDescription();
            mainArray[i][3] = thisStock.getSector();
            mainArray[i][4] = thisStock.getPrice();
            mainArray[i][5] = thisStock.getDividend();
            i++;//increase index
        }
        return mainArray;
    }

    public String getAllStockInfo(){
        String outputString = "";
        String formatString = "%s (%s). $%.2f/share. Div yield $%.2f (%.2f%%). Sector: %s. Description: %s.";
        for (int i=0; i<stocks.length;i++){
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
