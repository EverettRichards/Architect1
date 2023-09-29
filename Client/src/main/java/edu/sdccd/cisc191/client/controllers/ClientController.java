package edu.sdccd.cisc191.client.controllers;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.io.InputStreamReader;

@Controller
public class ClientController {

    public static String getRequest(String urlToRead) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
        } catch(MalformedURLException e) {
            System.err.println(e);
        } catch(IOException e) {
            System.err.println(e);
        }
        return result.toString();
    }

    @GetMapping("/")
    public String index() {
        System.out.println(getRequest("https://localhost:8080/api/stocks"));
        return "index";
    }
}
