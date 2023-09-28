package edu.sdccd.cisc191.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Client {

    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
/*            Stock[] stocks = restTemplate.getForObject(
                    "http://localhost:8080/stocks", Stock[].class);

            for(Stock stock : stocks) {
                System.out.println(stock.getName());
            }*/
            System.out.println("Listening on port 8080.");
        };
    }
}