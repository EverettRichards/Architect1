package edu.sdccd.cisc191.server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.io.IOException;

/* This class implements needed API from finnhub
 * 
 */
public class Finnhub {
    private static String apikey = "get from environment variable";
    private String baseURL = "https://lmfao.com";

    public Finnhub(String key) {
        apikey = key;
    }

    private String readFile(String filePath) {
        Path path = Paths.get(filePath);

        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println(e.toString());
            return null;
        }
    }

    public String getCandle(String company) {
        String data = this.readFile("mock_data/candle.json");
        return data;
    }
}