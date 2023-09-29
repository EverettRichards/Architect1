package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.models.UIStock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class ClientController {

    RestTemplate restTemplate = new RestTemplate();
    String resourceURL = "http://localhost:8080/api";

    @GetMapping("/")
    public String index() {

        List<UIStock> stocks = restTemplate.getForObject(
                resourceURL + "/stocks",
                List.class
        );

        System.out.println(stocks);
        return "index";
    }
}
