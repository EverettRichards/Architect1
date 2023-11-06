package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.common.entities.Stock;
//import edu.sdccd.cisc191.common.entities.User;
import edu.sdccd.cisc191.common.entities.DataFetcher;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedList;
import java.util.List;

/**
 * ClientController - a program to create a webpage to display the stock information
 */
@Controller
public class ClientController {

//    public static String toJson(Object jsonObject) throws JsonProcessingException {
//        return new ObjectMapper().writeValueAsString(jsonObject);
//    }

    /**
     * Sets up to display the index
     * @return index the index page
     */
    @GetMapping("/")
    public String index() { return "index"; }

}
