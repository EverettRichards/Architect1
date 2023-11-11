package edu.sdccd.cisc191.common.entities;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * DataFetcher is an interface that
 * is used to define class structures
 * that perform REST API CRUD operations.
 */
public interface DataFetcher {

    final static String backendEndpointURL = "http://localhost:8081";

    final static String frontendEndpointURL = "http://localhost:8080";

    final static String apiEndpointURL = "/api";

    final static String userEndpointURL = "/api/user";

    final static String finnhubBaseURL = "https://finnhub.io/api/v1";

    final static String finnhubKey = "bsq5ig8fkcbcavsjbrrg";

    final static ArrayList<String> allTickers = new ArrayList<>() {
        {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("Common/src/main/static_data/all_tickers.txt"));
                String line;
                while ((line = reader.readLine()) != null) {
                    add(line);
                }
                reader.close();
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }

            // sort string alphabetically
            // sort(String::compareToIgnoreCase);
        }
    };
}
