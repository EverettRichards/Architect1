package edu.sdccd.cisc191.common.entities;

public class TestAdminUser {
    public static void main(string[] args){
        Stock apple = new Stock("AAPL");
        Stock amazon = new Stock("AMZN");
        Stock microsoft = new Stock("MSFT");
        Stock[] myStockList = {apple,amazon,microsoft};
        StockList techStocks = new StockList(myStockList);
        String totalOutput = techStocks.getAllStockInfo();
        System.out.println(totalOutput);
    }
}
