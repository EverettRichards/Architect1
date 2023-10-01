package edu.sdccd.cisc191.common.entities;

/**
 * StockList a program to create a list of stocks to be used to display on the webpage
 */
public class StockList {
    private Stock[] stocks;         //the stocks that will make up the list of stocks
    private User owner;             //the user that will be tracking a set of stocks on the webpage

    public static void main(String[] args) {
        System.out.println("Test");
    }

    /**
     * Constructor that creates a new StockList based on the array of stocks passed in
     * @param conveyedStocks the array of stocks to be added to the list
     */
    public StockList(Stock[] conveyedStocks) {
        stocks = new Stock[conveyedStocks.length];
        for (int i=0;i<conveyedStocks.length;i++){
            stocks[i] = conveyedStocks[i];
        }
    }

    /**
     * Gets the stock tickers
     * @return outputString the stock info in a string format
     */
    public String getTickers(){
        String outputString = "";
        for (int i=0;i<stocks.length;i++){
            outputString = outputString + stocks[i].getTicker();
            if (i<stocks.length-1){
                outputString = outputString + ",";
            }
        }
        return outputString;
    }

    /**
     * Gets the list of stocks and returns them as a 2d array
     * @return mainArray the stock list in the 2d array
     */
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

    /**
     * Gets all the stock info that available and formats it into a String from
     * concatenating the individual pieces that make up a stock listing
     * @return outputString the stock info in string format
     */
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
