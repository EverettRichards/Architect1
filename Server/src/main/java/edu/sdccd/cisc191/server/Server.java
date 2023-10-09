package edu.sdccd.cisc191.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.server.repositories.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

@SpringBootApplication
public class Server {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        //SpringApplication.run(Server.class, args);

        String[] myTickers = {"AAPL","DIS","BAC","UAA","CCL","KO","WMT","T","GOOGL","MSFT","V","NVDA","AMZN","COST","AMD","TSM","META","TSLA"};
        for (String ticker : myTickers){
            Stock myStock = StockIO.loadStock(ticker);
            myStock.setPrice(myStock.getPrice()+20);
            StockIO.saveStock(myStock);
        }
    }

    @Bean
    public CommandLineRunner demo(StockRepository repository) {
        return (args) -> {
            // save a few customers
            /*repository.save(new Stock("AAPL"));
            repository.save(new Stock("TSLA"));
            repository.save(new Stock("NVDA"));
            repository.save(new Stock("AMZN"));*/
        };
    }
}
