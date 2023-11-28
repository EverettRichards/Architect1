package edu.sdccd.cisc191.client.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.cryptography.SessionCookie;
import edu.sdccd.cisc191.common.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import edu.sdccd.cisc191.common.entities.DataFetcher;

import static edu.sdccd.cisc191.common.entities.DataFetcher.backendEndpointURL;
import static edu.sdccd.cisc191.common.entities.DataFetcher.userEndpointURL;

public class MyDatabaseUserDetailsService implements UserDetailsService {
    private final String baseURL = backendEndpointURL + userEndpointURL;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // (1)
        // 1. Load the user from the users table by username. If not found, throw UsernameNotFoundException.
        // 2. Convert/wrap the user to a UserDetails object and return it.
        ResponseEntity<String> response;
        AuthUser user;

        try {
            response = restTemplate.exchange(
                    baseURL + "/name/" + username,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if(response.getStatusCode() != HttpStatus.OK) {
                throw new UsernameNotFoundException("User not found.");
            }
        } catch(ClassCastException e) {
            System.err.println(e.toString());
            throw new InvalidPayloadException();
        } catch(RestClientException e) {
            throw new UsernameNotFoundException("User not found.");
        }
        try {
            user = new ObjectMapper().readValue(response.getBody(),
                    AuthUser.class);
        } catch(JsonProcessingException e) {
            throw new UsernameNotFoundException("User not found.");
        }

        return user;
    }
}
