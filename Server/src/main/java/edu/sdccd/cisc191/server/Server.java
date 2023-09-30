package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.server.repositories.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Server {

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
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
