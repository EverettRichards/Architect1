package edu.sdccd.cisc191.client.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DefaultStocksFileIO {

    public static final String defaultStocks = "src/main/resources/defaultStocks.txt";
    private ArrayList<String> stocks = new ArrayList<>();

    public int readAndUpdateDefaultStocks(String readFile) {
        Scanner file;
        try {
            file = new Scanner( new File(readFile));
        } catch(FileNotFoundException e) {
            return -1;
        }

        while(file.hasNextLine()) {
            stocks.add(file.nextLine());
        }

        return 0;
    }

    public ArrayList<String> getDefaultStocks() {
        return this.stocks;
    }
}
