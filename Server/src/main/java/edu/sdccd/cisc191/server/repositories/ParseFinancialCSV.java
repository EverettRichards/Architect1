package edu.sdccd.cisc191.server.repositories;

import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockList;

public class ParseFinancialCSV {
    public static void main(String[] args){
        Stock apple = new Stock("AAPL","Apple","Makes iPhones and overpriced headphones","Tech",296.50,2.39);
        Stock amazon = new Stock("AMZN","Amazon","A worldwide distribution company","IT",3096.30,5.43);
        Stock microsoft = new Stock("MSFT","Miscorosft","A major tech company","IT",495.29,0.50);
        Stock[] myStockList = {apple,amazon,microsoft};
        StockList techStocks = new StockList(myStockList);
        String totalOutput = techStocks.getAllStockInfo();
        System.out.println(techStocks.getTickers());
        System.out.println(totalOutput);
    }

    public ParseFinancialCSV(String fileAddress){

    }
}
