package edu.sdccd.cisc191.client.controllers;

import edu.sdccd.cisc191.client.models.DefaultStocksFileIO;
import edu.sdccd.cisc191.client.models.NewUser;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String myAccount(Model model, HttpServletRequest request) {
        //For getting user information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        HttpSession session = request.getSession();

        User user;

        if (session.getAttribute("user") != null) {
            user = (User) session.getAttribute("user");
            System.out.println("Session User Found: " + user.getName());
        } else {
            ResponseEntity<User> fetchUser;
            try {
                fetchUser = restTemplate.exchange(
                        baseURL + "/user/name/" + currentPrincipalName,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );
            } catch (Exception error) {
                model.addAttribute("error", "Something went wrong!");
                return "/dashboard";
            }

            user = fetchUser.getBody();
            System.out.println("Session User Created: " + user.getName());

            session.setAttribute("user", user);
        }

        model.addAttribute("user", user);
        return "myaccount";
    }

    @GetMapping("/my-account/edit")
    public String editMyAccount(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        NewUser editUser = new NewUser();
        model.addAttribute("user", user);
        model.addAttribute("editUser", editUser);
        return "myaccount-edit";
    }
}
