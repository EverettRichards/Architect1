package edu.sdccd.cisc191.server.repositories;

import com.opencsv.exceptions.CsvValidationException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockList;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

    public ParseFinancialCSV(String fileAddress) throws IOException, CsvValidationException {
        try (FileReader fr = new FileReader(fileAddress,StandardCharsets.UTF_8);
             CSVReader reader = new CSVReader(fr)) {

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                for (String e : nextLine) {
                    System.out.println(e);
                }
            }

        }
    }
}
