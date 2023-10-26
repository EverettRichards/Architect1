package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.common.entities.Stock;
import edu.sdccd.cisc191.common.entities.DataFetcher;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    String resourceURL = DataFetcher.backendEndpointURL + DataFetcher.apiEndpointURL;

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

    @GetMapping("/sign-up")
    public String signUp() {
        return "signup";
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "signin";
    }

    /**
     * Sets up to display the dashboard
     * @param model model to display stocks
     * @return dashboard the dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        //Declare a variable for stocks to pass in to template
        List<Stock> stocks;

        //restTemplate.getForObject
        try {
            ResponseEntity<List<Stock>> response = restTemplate.exchange(
                    resourceURL + "/stocks",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            stocks = response.getBody();
        } catch (Exception e) {
            stocks = null;
        }

        model.addAttribute("stocks", stocks);

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
        //Stock variable for passing into template
        Stock stock;

        try {
            ResponseEntity<Stock> response = restTemplate.exchange(
                    resourceURL + "/stocks/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            stock = response.getBody();
        } catch (Exception e) {
            stock = null;
        }

        model.addAttribute("stock", stock);
        return "stock";
    }
}
