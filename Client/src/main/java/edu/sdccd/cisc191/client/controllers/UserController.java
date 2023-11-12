package edu.sdccd.cisc191.client.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.sdccd.cisc191.common.cryptography.SessionCookie;
//import org.springframework.web.client.RestTemplate;
import edu.sdccd.cisc191.common.entities.DataFetcher;
//import edu.sdccd.cisc191.common.entities.User;
//
//import java.util.Map;

@Controller
public class UserController implements DataFetcher {

//    private final String baseURL = backendEndpointURL + userEndpointURL;
//    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/sign-in")
    public String signIn(@CookieValue(value = "token", required = false) String sessionCookie) {
        if(sessionCookie == null) {
            return "signin";
        }

        SessionCookie cookie = new SessionCookie(sessionCookie);
        if(cookie.isValid()) {
            return "dashboard";
        }

        return "signin";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }

    //GET Sign Up HTML template handler
    @GetMapping("/sign-up")
    public String signUp(@CookieValue(value = "token", required = false) String sessionCookie) {
        if(sessionCookie == null) {
            return "signin";
        }

        SessionCookie cookie = new SessionCookie(sessionCookie);
        if(cookie.isValid()) {
            return "dashboard";
        }
        return "signup";
    }

    @GetMapping("/my-account")
    public String myAccount() {
        return "myaccount";
    }
}
