package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.Ticker;
import edu.sdccd.cisc191.common.errors.BadTickerException;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.io.*;

public class ServerStock extends Stock {
    public static final int stockJsonVersion = 3; // The internal version of the Stock.java JSON files.
    // If you make any changes to the expected/actual format of JSON stock files, please add 1 to this value.


    // Update the attributes of the Stock using a JSON data structure. Used for instantiation
    // once valid JSON is found either from the API or the server.
    private void updateFromJsonNode(JsonNode root, Boolean updateTimeStamp) {
        setName(root.get("name").asText());
        setSector(root.get("sector").asText());
        setDescription(String.format("%s is a company in the %s sector.",getName(),getSector()));
        setPrice(root.get("price").asDouble());
        setLastRefresh(root.get("last_updated").asLong());
        setDividend(0);

        if (updateTimeStamp) {
            // Update this so we know when the stock info needs to be renewed
            setLastRefresh(System.currentTimeMillis());
        }
    }

    // Call the API to get the latest information. Then, save the new data to the server.
    private void updateFromAPI() throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        String ticker = getTicker();
        String finnhubResult = FinnhubNetworking.getJsonFromFinnhub(ticker);
        JsonNode root = new JsonObject(finnhubResult).getJsonNode();;
        updateFromJsonNode(root,true);
        saveAsJsonFile();
    }

    // Opens the corresponding JSON file to see if it's good for using.
    private void updateFromFile() throws IOException {
        FileInputStream stream = new FileInputStream(Server.stockDirectory+"/"+getTicker()+".json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String contents = reader.readLine();
        reader.close();
        stream.close();

        JsonNode root = new JsonObject(contents).getJsonNode();;

        if (root.get("json_version").asInt() != stockJsonVersion) {
            // Stock has an old version. Don't use it.
            System.out.println("Updating old-versioned stock.");
            updateFromAPI();
        } else if (System.currentTimeMillis() - root.get("last_updated").asLong() >= (1000L* FinnhubNetworking.secondsBeforeRefreshNeeded)) {
            // Stock hasn't been updated recently. Don't use it.
            System.out.println("Updating outdated stock.");
            updateFromAPI();
        } else {
            // The stock is up-to-date. Load it WITHOUT an API call.
            updateFromJsonNode(root,false);
        }
    }

    // Updates an existing Stock object using the best method available (stored file or API).
    // Also used to initialize a brand new Stock object.
    public void updateFromBestMethod() throws MalformedURLException, JsonProcessingException, FileNotFoundException {
        try {
            // Attempt to instantiate the Stock using data from a JSON file on the server.
            updateFromFile();
        } catch (IOException e) {
            // If there was no file to open, use the API and then create a file.
            System.out.println("File not found (Stock). Find from API.");
            updateFromAPI();
        }
    }

    // Creates a new stock using the best data. Like a constructor, but defined outside of Stock.java itself.
    // Will use an existing file OR the FinnHub API, depending on the availability and recency of a relevant file.
    public ServerStock(String ticker) throws MalformedURLException, JsonProcessingException, BadTickerException {
        Ticker ticker_object = new Ticker(ticker);
        setTicker(ticker_object.getTicker());
        try {
            // Attempt to update the freshly instantiated stock
            updateFromBestMethod();
        } catch(Exception e) {
            // Failed!
        }
    }

    // Returns a String containing the attributes of a Stock object.
    public String toJson() throws JsonProcessingException {
        ObjectMapper map = new ObjectMapper();
        ObjectNode parent = map.createObjectNode();

        parent.put("name",getName());
        parent.put("sector",getSector());
        parent.put("description",getDescription());
        parent.put("price",getPrice());
        parent.put("dividend",0);
        parent.put("last_updated",getLastRefresh());
        parent.put("json_version",stockJsonVersion);

        return new JsonObject(parent).getString();
    }

    // Creates a file, TICKER.json, containing the JSON form of a provided Stock object
    public void saveAsJsonFile() throws JsonProcessingException, FileNotFoundException {
        System.out.println("Saved a stock!!");
        String json = toJson();
        new PrintWriter(Server.stockDirectory+"/"+getTicker()+".json").print(json);
    }
}
