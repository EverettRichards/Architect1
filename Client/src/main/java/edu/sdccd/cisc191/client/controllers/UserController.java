package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.models.DefaultStocksFileIO;
import edu.sdccd.cisc191.client.models.NewUser;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Controller
public class UserController implements DataFetcher {

    private final String baseURL = backendEndpointURL + userEndpointURL;
    private RestTemplate restTemplate = new RestTemplate();

    //Render sign in page
    @GetMapping("/sign-in")
    public String signIn() {
        return "signin";
    }

    //Render sign in success page
    @GetMapping("/sign-in/success")
    public String signInSuccess() {
        return "signin-success";
    }

    //Render sign up page
    @GetMapping("/sign-up")
    public String signUp(Model model) {
        NewUser newUser = new NewUser();
        model.addAttribute("user", newUser);
        return "signup";
    }

    //Handle sign up form submission and conditionally render success page or error.
    @PostMapping("sign-up")
    public String signUpNewUser(@ModelAttribute("user") NewUser newUser, Model model) {
        String passwordHash = Hasher.hashNewPassword(newUser.getPassword());

        //Read from file i/o for a default list of stock tickers to follow.
        DefaultStocksFileIO defaultStocks = new DefaultStocksFileIO();
        defaultStocks.readAndUpdateDefaultStocks();
        ArrayList<String> tickers = defaultStocks.getDefaultStocks();

        User userSignUp = new User(newUser.getEmail(), newUser.getUsername(), newUser.getNickname(), passwordHash, User.Role.Regular, tickers);
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(
                    baseURL + "/add",
                    HttpMethod.POST,
                    new HttpEntity<User>(userSignUp),
                    new ParameterizedTypeReference<>() {}
            );

            if(response.getStatusCode().is4xxClientError()) {
                model.addAttribute("error", "Username taken, please try a different username.");
                return "signup";
            }
        } catch (Exception error) {
            model.addAttribute("error", "There was an error processing the request.");
            return "signup";
        }
        return "signup-success";
    }

    //Return user account page
    @GetMapping("/my-account")
    public String myAccount() {
        return "myaccount";
    }
}
