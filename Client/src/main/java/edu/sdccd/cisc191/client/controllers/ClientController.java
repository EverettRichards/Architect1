package edu.sdccd.cisc191.client.controllers;

import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class ClientController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
