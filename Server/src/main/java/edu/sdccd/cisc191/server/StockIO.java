package edu.sdccd.cisc191.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.*;

import org.apache.commons.text.StringEscapeUtils;

import edu.sdccd.cisc191.common.entities.Stock;
import org.w3c.dom.*;
import org.xml.sax.*;

public class StockIO {
    /*public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        Stock myStock = new Stock("AAPL");
        // System.out.println(myStock.getTicker());

        saveStock("output.xml",myStock);

        Stock outputStock = xmlToStock("output.xml");
        outputStock.setPrice(390.10);
        outputStock.setDividend(4.51);

        saveStock("output2.xml",outputStock);
    }*/

    private static long secondsBeforeRefreshNeeded = 60; // number of seconds before a cached stock will be forced to refresh
    private static final String dataDirectory = "Server/data/";

    public static String getStockFilePointer(String ticker){
        return dataDirectory + ticker + ".xml";
    }

    public static String escapeXml(String input){
        return StringEscapeUtils.escapeXml11(input);
    }

    public static void saveStock(String reference, Stock stock) throws FileNotFoundException {
        String toXML = stockToXML(stock);
        createFile(reference,toXML);
    }

    public static void saveStock(Stock stock) throws FileNotFoundException {
        String toXML = stockToXML(stock);
        String reference = getStockFilePointer(stock.getTicker());
        createFile(reference,toXML);
    }

    public static String stockToXML(Stock stock) {
        String output = "";

        output += "<?xml version=\"1.0\"?>\n";
        output += "<stock version=\"2.0\">\n";
        output += "\t<id>" + stock.getId() + "</id>\n";
        output += "\t<lastRefresh>" + stock.getLastRefresh() + "</lastRefresh>\n";
        output += "\t<ticker>" + escapeXml(stock.getTicker()) + "</ticker>\n";
        output += "\t<name>" + escapeXml(stock.getName()) + "</name>\n";
        output += "\t<description>" + escapeXml(stock.getDescription()) + "</description>\n";
        output += "\t<stockSector>" + escapeXml(stock.getSector()) + "</stockSector>\n";
        output += "\t<sharePrice val=\"" + stock.getPrice() + "\"/>\n";
        output += "\t<dividendYield val=\"" + stock.getDividend() + "\"/>\n";
        output += "</stock>";

        return output;
    }

    public static void createFile(String path, String filename, String content) throws FileNotFoundException {
        createFile(path + "\\" + filename, content);
    }

    public static void createFile(String reference, String content) throws FileNotFoundException {
        File myFile = new File(reference);
        FileReader data;
        PrintWriter result = new PrintWriter(reference);

        result.print(content);
        result.close();
    }

    public static String readFile(String reference) {
        return "";
    }

    /*
    * loadStock takes a String ticker (i.e. "AAPL")
    * and returns a Stock object using the saved data, if it exists
    * OR creates a new Stock object with API data, if one does not
    * exist in storage.
     */
    public static Stock loadStock(String ticker) throws ParserConfigurationException, SAXException, IOException {
        Stock outputStock;
        try{ // Attempt to recall the stock from memory, unless too much time has passed.
            outputStock = xmlToStock(getStockFilePointer(ticker));
            System.out.println("Stock creation successful based on stored stock.");

            // If too much time has passed since the last refresh, update the stock
            // before passing it on to the Server method that called it.
            long currentTime = System.currentTimeMillis();
            if (currentTime - outputStock.getLastRefresh() > secondsBeforeRefreshNeeded*1000){
                System.out.println("This stock has not been updated for too long. Force update.");
                outputStock.Update();
            }
        }
        catch(Exception e){
            outputStock = new Stock(ticker); // Create a new Stock object, initialized with API data
            System.out.println("Exception occurred. New Stock object being created.");
            // Now, save the brand new stock so that it can be used later.
            saveStock(outputStock);
        }

        return outputStock;
    }

    /*
    *
     */
    public static Stock xmlToStock(String reference) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmldoc = docReader.parse(new File(reference));
        Element root = xmldoc.getDocumentElement();

        long id = Long.parseLong(root.getElementsByTagName("id").item(0).getTextContent());
        long lastRefresh = Long.parseLong(root.getElementsByTagName("lastRefresh").item(0).getTextContent());

        String ticker = root.getElementsByTagName("ticker").item(0).getTextContent();
        String name = root.getElementsByTagName("name").item(0).getTextContent();
        String description = root.getElementsByTagName("description").item(0).getTextContent();
        String stockSector = root.getElementsByTagName("stockSector").item(0).getTextContent();

        Element priceElement = (Element) root.getElementsByTagName("sharePrice").item(0);
        double price = Double.parseDouble(priceElement.getAttribute("val"));

        Element divElement = (Element) root.getElementsByTagName("dividendYield").item(0);
        double dividend = Double.parseDouble(divElement.getAttribute("val"));

        Stock outputStock = new Stock(id,lastRefresh,ticker,name,description,stockSector,price,dividend);
        return outputStock;
    }

    // data persistence

    // save a file version of a stock
}