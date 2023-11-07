package edu.sdccd.cisc191.server;

//import edu.sdccd.cisc191.common.entities.Stock;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.Requests;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.net.MalformedURLException;

public class FinnhubNetworking {

    private static final String token = DataFetcher.finnhubKey;

    public static long secondsBeforeRefreshNeeded = 60; // number of seconds before a cached stock will be forced to refresh

    private static ExecutorService mainExecutor = Executors.newFixedThreadPool(10);

    private static int callsSinceRefresh = 0;
    private static long lastUpdateTime = 0;

    public static String fetchData(String url) {
        try {
            callsSinceRefresh++;
            long timeDifference = TimeMethods.now() - lastUpdateTime;
            if (callsSinceRefresh >= 30 && timeDifference <= 1000L) {
                Thread.sleep(1000L - timeDifference);
                callsSinceRefresh = 0;
            }
            lastUpdateTime = TimeMethods.now();
            String result = Requests.get(url);
            return result;

        } catch (InterruptedException e) {
            System.out.println("Critical error encountered with API concurrency.");
            return "ERROR";
        } catch (MalformedURLException e) {
            return "ERROR";
        }
    }

    public static String fetchData(String url, long t) throws MalformedURLException {
        return Requests.get(url);
    }

    public static String getJsonFromFinnhub(String ticker) throws MalformedURLException, JsonProcessingException {
        String companyInfoJson = fetchData("https://finnhub.io/api/v1/stock/profile2?symbol="
                    + ticker + "&token=" + token);
        String stockPriceJson = fetchData("https://finnhub.io/api/v1/quote?symbol="
                    + ticker + "&token=" + token);
        return DataMethods.mergeStockJson(companyInfoJson, stockPriceJson);
    }

    // The static list of index attributes we want to request from FinnHub API
    public static String getCandleFromFinnhub(String ticker, String duration, long time1, long time2) throws MalformedURLException, JsonProcessingException {
        String resolution = TimeMethods.getFrequency(duration);
        String URL = "https://finnhub.io/api/v1/stock/candle?symbol=" + ticker
                + "&resolution=" + resolution
                + "&from=" + time1 + "&to=" + time2
                + "&token=" + DataFetcher.finnhubKey;
        return DataMethods.annotateCandles(fetchData(URL), ticker, duration, time1, time2);
    }
}
