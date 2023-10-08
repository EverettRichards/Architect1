package edu.sdccd.cisc191.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.*;

import edu.sdccd.cisc191.common.entities.Stock;
import org.w3c.dom.*;
import org.xml.sax.*;

public class StockIO {
    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
        Stock myStock = new Stock("AAPL");
        // System.out.println(myStock.getTicker());

        saveStock("output.xml",myStock);

        Stock outputStock = xmlToStock("output.xml");
        outputStock.setPrice(390.10);
        outputStock.setDividend(4.51);

        saveStock("output2.xml",outputStock);
    }

    public static void saveStock(String reference, Stock stock) throws FileNotFoundException {
        String toXML = stockToXML(stock);
        createFile(reference,toXML);
    }

    public static String stockToXML(Stock stock) {
        String output = "";

        output += "<?xml version=\"1.0\"?>\n";
        output += "<stock version=\"2.0\">\n";
        output += "\t<id>" + stock.getId() + "</id>\n";
        output += "\t<lastRefresh>" + stock.getLastRefresh() + "</lastRefresh>\n";
        output += "\t<ticker>" + stock.getTicker() + "</ticker>\n";
        output += "\t<name>" + stock.getName() + "</name>\n";
        output += "\t<description>" + stock.getDescription() + "</description>\n";
        output += "\t<stockSector>" + stock.getSector() + "</stockSector>\n";
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