package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.common.entities.Stock;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * ClientController - a program to create a webpage to display the stock information
 */
@Controller
public class ClientController {

    RestTemplate restTemplate = new RestTemplate();
    String resourceURL = "http://localhost:8080/api";

    public static String toJson(Object jsonObject) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(jsonObject);
    }

    /**
     * Sets up to display the index
     * @return index the index page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Sets up to display the dashboard
     * @param model model to display stocks
     * @return dashboard the dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        //restTemplate.getForObject
        ResponseEntity<List<Stock>> response = restTemplate.exchange(
                resourceURL + "/stocks",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<Stock> stocks = response.getBody();

        model.addAttribute("stocks", stocks);

//        try {
//            System.out.println(toJson(stocks));
//        } catch(JsonProcessingException e) {
//            System.err.println(e);
//        }

        return "dashboard";
    }

    /**
     * Sets up to display the stock page
     * @param id the Long id that identifies each stock
     * @param model the method to create the stock listing
     * @return stock the stock page
     */
    @GetMapping("/dashboard/stock/{id}")
    public String stockDetails(@PathVariable("id") Long id, Model model) {
        ResponseEntity<Stock> response = restTemplate.exchange(
                resourceURL + "/stocks/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Stock stock = response.getBody();

        System.out.println(stock);
        model.addAttribute("stock", stock);
        return "stock";
    }
}
