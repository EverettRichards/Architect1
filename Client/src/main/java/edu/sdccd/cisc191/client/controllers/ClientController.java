package edu.sdccd.cisc191.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.client.models.ClientFetcher;
import edu.sdccd.cisc191.client.models.UIStock;
import org.springframework.asm.TypeReference;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;


import java.net.URL;
//import java.nio.charset.MalformedInputException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.io.InputStreamReader;
import java.util.List;

@Controller
public class ClientController {

    @GetMapping("/")
    public String index() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String stocksString = ClientFetcher.getRequest("http://localhost:8080/api/stocks");

        System.out.println(stocksString);
        return "index";
    }
}
