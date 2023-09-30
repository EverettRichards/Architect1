package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.common.Stock;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Controller
public class ClientController {

    RestTemplate restTemplate = new RestTemplate();
    String resourceURL = "http://localhost:8080/api";

    public static String toJson(Object jsonObject) throws JsonProcessingException {
        String json = new ObjectMapper().writeValueAsString(jsonObject);
        return json;
    }

    @GetMapping("/")
    public String index() {

        //restTemplate.getForObject
        ResponseEntity<List<Stock>> response = restTemplate.exchange(
                resourceURL + "/stocks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Stock>>() {}
        );

        List<Stock> stocks = response.getBody();

        try {
            System.out.println(toJson(stocks));
        } catch(JsonProcessingException e) {
            System.err.println(e);
        }

        return "index";
    }
}
