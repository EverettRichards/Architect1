package edu.sdccd.cisc191.server.repositories;

import com.opencsv.exceptions.CsvValidationException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockList;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ParseFinancialCSV {
    public static void main(String[] args) throws IOException, CsvValidationException{
        /*Stock apple = new Stock("AAPL","Apple","Makes iPhones and overpriced headphones","Tech",296.50,2.39);
        Stock amazon = new Stock("AMZN","Amazon","A worldwide distribution company","IT",3096.30,5.43);
        Stock microsoft = new Stock("MSFT","Miscorosft","A major tech company","IT",495.29,0.50);
        Stock[] myStockList = {apple,amazon,microsoft};
        StockList techStocks = new StockList(myStockList);
        String totalOutput = techStocks.getAllStockInfo();
        System.out.println(techStocks.getTickers());
        System.out.println(totalOutput);*/
        ParseCSV("Server/src/main/resources/SP500in2018.csv");
    }
    public static void ParseCSV(String fileAddress) throws IOException, CsvValidationException {
        try (FileReader fr = new FileReader(fileAddress,StandardCharsets.UTF_8);
             CSVReader reader = new CSVReader(fr)) {
            String[] nextLine;
            reader.readNext();
            Stock[] theseStocks = new Stock[1000];
            int i = 0;
            while ((nextLine = reader.readNext()) != null) {
                Stock thisStock = new Stock(nextLine[0],nextLine[1],"Generic company",nextLine[2],Double.parseDouble(nextLine[3]),Double.parseDouble(nextLine[5]));
                theseStocks[i]=thisStock;
                i++;
            }
            Stock[] theseAdjustedStocks = new Stock[i];
            for (int j=0; j<i; j++) {
                theseAdjustedStocks[j] = theseStocks[j];
            }
            StockList thisStockList = new StockList(theseAdjustedStocks);
            System.out.println(thisStockList.getAllStockInfo());
        }
    }
    public ParseFinancialCSV(){

    }
}
