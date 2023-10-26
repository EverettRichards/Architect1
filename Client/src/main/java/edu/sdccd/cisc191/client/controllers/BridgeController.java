package edu.sdccd.cisc191.client.controllers;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sdccd.cisc191.client.errors.FrontendException;
import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.client.errors.UsernameTakenException;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.User;

// bridging client with backend api

@RestController
@RequestMapping("/api")
public class BridgeController implements DataFetcher {
    private final String baseURL = backendEndpointURL + userEndpointURL;
    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> payload) throws FrontendException {
        String email;
        String username;
        String nickname;
        String password;

        try {
            email = payload.get("email");
            username = payload.get("username");
            nickname = payload.get("nickname");
            password = payload.get("password");
        } catch(ClassCastException | NullPointerException e) {
            throw new InvalidPayloadException();
        }

        if(email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            throw new InvalidPayloadException();
        }

        String passwordHash = Hasher.hashNewPassword(password);

        User newUser = new User(email, username, nickname, passwordHash, User.Role.Regular);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                baseURL + "/add", 
                HttpMethod.POST, 
                new HttpEntity<User>(newUser), 
                new ParameterizedTypeReference<>() {}
            );

            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            System.out.println(response.getStatusCode());

            if(response.getStatusCode().is4xxClientError()) {
                throw new UsernameTakenException();
            }
        } catch(ClassCastException e) {
            System.err.println(e.toString());
            throw new InvalidPayloadException();
        }

        return new ResponseEntity<String>("Account created successfully", null, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> payload) throws InvalidPayloadException {
        String username;
        String password;

        try {
            username = payload.get("username");
            password = payload.get("password");
        } catch(ClassCastException | NullPointerException e) {
            throw new InvalidPayloadException();
        }

        if(username.isEmpty() || password.isEmpty()) {
            throw new InvalidPayloadException();
        }

        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(
                baseURL + "/name/" + username,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
            );

            System.out.println(response.getStatusCode());
            
            if(response.getStatusCode().equals(200)) {
                return new ResponseEntity<String>("Invalid username or password", null, 0);
            }
        } catch(ClassCastException e) {
            System.err.println(e.toString());
            throw new InvalidPayloadException();
        }

        try {
            User user = new ObjectMapper().readValue(response.getBody(), User.class);
            if(Hasher.isCorrectPassword(user, password)) {
                return new ResponseEntity<String>("logged in", null, HttpStatus.OK);
            }
        } catch(JsonProcessingException e) {
            return new ResponseEntity<String>("User values are corrupted in database.", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<String>("invalid username or password", null, HttpStatus.FORBIDDEN);
    }
}