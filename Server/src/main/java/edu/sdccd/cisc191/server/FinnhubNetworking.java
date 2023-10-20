package edu.sdccd.cisc191.server;

//import edu.sdccd.cisc191.common.entities.Stock;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.Requests;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.StockCandle;

import java.net.MalformedURLException;

public class FinnhubNetworking {

    private static final String token = DataFetcher.finnhubKey;

    public static long secondsBeforeRefreshNeeded = 60; // number of seconds before a cached stock will be forced to refresh

    private final static String[] jsonInput1 = new String[]{"name","finnhubIndustry","name"};
    private final static String[] jsonOutput1 = new String[]{"name","finnhubIndustry","name"};
    private final static String[] jsonInput2 = new String[]{"name","finnhubIndustry","name"};
    private final static String[] jsonOutput2 = new String[]{"name","finnhubIndustry","name"};

    public static String getJsonFromFinnhub(String ticker) throws MalformedURLException, JsonProcessingException {
        String companyInfoJson = Requests.get("https://finnhub.io/api/v1/stock/profile2?symbol="
                + ticker + "&token=" + token);
        String stockPriceJson = Requests.get("https://finnhub.io/api/v1/quote?symbol="
                + ticker + "&token=" + token);
        return DataMethods.mergeStockJson(companyInfoJson,stockPriceJson);
    }
    public static void UpdateStock(Stock stock) throws JsonProcessingException, MalformedURLException {
        // Get the JSON data from Finnhub with basic company info such as name, sector, etc.
        String rawJson = getJsonFromFinnhub(stock.getTicker());

        JsonNode root = DataMethods.decodeJson(rawJson);

        // Update core attributes accordingly
        stock.setName(root.get("name").asText());
        stock.setSector(root.get("finnhubIndustry").asText());
        stock.setDescription(String.format("%s is a company in the %s sector.",stock.getName(),stock.getSector()));

        // Update core attributes accordingly
        stock.setPrice(root.get("c").asDouble());

        stock.setLastRefresh(System.currentTimeMillis());
    }

    // Update the stock only if necessary
    public static void CheckForStockUpdates(Stock stock) throws JsonProcessingException, MalformedURLException {
        long lastRefresh = stock.getLastRefresh();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRefresh > secondsBeforeRefreshNeeded*1000){
            System.out.println("This stock has not been updated for too long. Force update.");
            UpdateStock(stock);
            // Soon, will add functionality to update the persistent form of the stock
        }
    }

    // The static list of index attributes we want to request from FinnHub API
    private static final String[] subKeys = {"c","h","l","o","t","v"};


    public static void UpdateCandle(StockCandle candle, String newTicker, String resolution, long time1, long time2)
            throws JsonProcessingException, MalformedURLException {
        String URL = "https://finnhub.io/api/v1/stock/candle?symbol="+newTicker
                +"&resolution="+resolution
                +"&from="+time1+"&to="+time2
                +"&token="+DataFetcher.finnhubKey;
        String jsonInput = Requests.get(URL);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonInput);

        double[][] result = new double[subKeys.length][];

        int keyIndex = 0;

        for (String subKey : subKeys) {

            JsonNode tNode = rootNode.get(subKey);
            result[keyIndex] = new double[tNode.size()];

            for (int i = 0; i < tNode.size(); i++) {
                result[keyIndex][i] = tNode.get(i).asDouble();
            }

            keyIndex++;

        }

        candle.setStockInfo(DataMethods.invert2DArray(result));
    }
}
