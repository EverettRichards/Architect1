package edu.sdccd.cisc191.client.models;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DefaultStocksFileIO {

    private String defaultStocks = "defaultStocks.txt";

    private ResourceLoader resourceLoader; //For reading files in resources folder.
    private ArrayList<String> stocks = new ArrayList<>();

    public int readAndUpdateDefaultStocks() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.defaultStocks);
//        System.out.println(inputStream);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            while(reader.ready()) {
                stocks.add(reader.readLine());
            }
            inputStream.close();
        } catch(IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    public ArrayList<String> getDefaultStocks() {
        return this.stocks;
    }
}
