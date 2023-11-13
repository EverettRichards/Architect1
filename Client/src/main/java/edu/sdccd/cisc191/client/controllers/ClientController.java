package edu.sdccd.cisc191.client.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
