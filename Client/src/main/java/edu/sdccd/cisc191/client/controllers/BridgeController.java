package edu.sdccd.cisc191.client.controllers;

import java.util.Map;

import edu.sdccd.cisc191.common.entities.StockCandle;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sdccd.cisc191.client.errors.FrontendException;
import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.client.errors.UsernameTakenException;
import edu.sdccd.cisc191.client.models.LoginForm;
import edu.sdccd.cisc191.client.models.RegisterForm;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.cryptography.SessionCookie;
import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.User;

// bridging client with backend api

@RestController
@RequestMapping("/api")
public class BridgeController implements DataFetcher {
    private final String baseURL = backendEndpointURL + userEndpointURL;
    private final String stockURL = backendEndpointURL + apiEndpointURL;
    private RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterForm form) throws FrontendException {
        String passwordHash = Hasher.hashNewPassword(form.getPassword());

        User newUser = new User(form.getEmail(), form.getUsername(), form.getNickname(), passwordHash, User.Role.Regular);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                baseURL + "/add", 
                HttpMethod.POST, 
                new HttpEntity<User>(newUser), 
                new ParameterizedTypeReference<>() {}
            );

            System.out.println(response.getStatusCode());

            if(response.getStatusCode().is4xxClientError()) {
                throw new UsernameTakenException();
            }
        } catch(ClassCastException e) {
            System.err.println(e.toString());
            throw new InvalidPayloadException();
        } catch(RestClientException e) {
            return new ResponseEntity<String>(e.getMessage(), null, HttpStatus.FORBIDDEN);
        }

        try {
            newUser = new ObjectMapper().readValue(response.getBody(), User.class);
        } catch(JsonProcessingException e) {
            return new ResponseEntity<String>("User values are corrupted in database.", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println(newUser.toString());

        String cookie = SessionCookie.createToken(newUser);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "token=" + cookie);
        headers.add(HttpHeaders.LOCATION, "/dashboard");

        return new ResponseEntity<String>("Account created successfully", headers, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm form) throws InvalidPayloadException {
        if(form.getUsername().isEmpty() || form.getPassword().isEmpty()) {
            throw new InvalidPayloadException();
        }

        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(
                baseURL + "/name/" + form.getUsername(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
            );
            
            if(response.getStatusCode() != HttpStatus.OK) {
                return new ResponseEntity<String>("Invalid username or password", null, HttpStatus.UNAUTHORIZED);
            }
        } catch(ClassCastException e) {
            System.err.println(e.toString());
            throw new InvalidPayloadException();
        } catch(RestClientException e) {
            return new ResponseEntity<String>(e.getMessage(), null, HttpStatus.FORBIDDEN);
        }

        try {
            User user = new ObjectMapper().readValue(response.getBody(), User.class);
            if(Hasher.isCorrectPassword(user, form.getPassword())) {
                String cookie = SessionCookie.createToken(user);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.SET_COOKIE, "token=" + cookie);
                headers.add(HttpHeaders.LOCATION, "/dashboard");

                return new ResponseEntity<String>("logged in", headers, HttpStatus.OK);
            }
        } catch(JsonProcessingException e) {
            return new ResponseEntity<String>("User values are corrupted in database.", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<String>("invalid username or password", null, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/stocks/candles/{ticker}")
    public Number[][] getCandles(@PathVariable String ticker, @RequestParam String resolution) {
        ResponseEntity<Number[][]> response;

        try {
            response = restTemplate.exchange(
                stockURL + "/stocks/candles/" + ticker + "?resolution=" + resolution,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
            );
        } catch(Exception e) {
            System.err.println(e);
            return null;
        }
        Number[][] body = response.getBody();
        return body;
    }

    /**
     * Gets the candle data and puts it into a ticker format to be displayed
     * @param ticker the stock information
     * @return data the candle data in 2d array
     */
//    @GetMapping("/stocks/candles/{ticker}")
//    public double[][] getCandles(@PathVariable String ticker) {
//        StockCandle candles;
//
//        try {
//            candles = new StockCandle(ticker);
//        } catch(Exception e) {
//            System.err.println(e);
//            return null;
//        }
//
//        double[][] data = candles.getStockInfo();
//        //System.out.println(candles.toString());
//        return data;
//    }
}