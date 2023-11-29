package edu.sdccd.cisc191.client.security;

import ch.qos.logback.core.boolex.Matcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.common.cryptography.Hasher;
import edu.sdccd.cisc191.common.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static edu.sdccd.cisc191.common.entities.DataFetcher.backendEndpointURL;
import static edu.sdccd.cisc191.common.entities.DataFetcher.userEndpointURL;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final String baseURL = backendEndpointURL + userEndpointURL;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Get the username and password from the authentication object
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Check if the user is authenticated
        if (authenticateUser(username, password)) {
            // Create an authentication object for the authenticated user
            return new UsernamePasswordAuthenticationToken(
                    username, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // This method is called to authenticate the user
    private boolean authenticateUser(String username, String password) {
        ResponseEntity<String> response;
        User user;
        try {
            response = restTemplate.exchange(
                    baseURL + "/name/" + username,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if(response.getStatusCode() != HttpStatus.OK) {
                System.out.println("If clause in first try block.");
                throw new UsernameNotFoundException("User not found.");
            }
        }
        catch(ClassCastException e) {
            System.out.println("Wrong typecast.");
            throw new InvalidPayloadException("Invalid payload.");
        } catch(RestClientException e) {
            System.out.println("Client Error.");
            throw new UsernameNotFoundException("Client Error.");
        }
        try {
            user = new ObjectMapper().readValue(response.getBody(),
                    User.class);
            // Check if the password is correct
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            if (!Hasher.isCorrectPassword(user, password)) {
                return false;
            }
            return true;
        } catch(JsonProcessingException e) {
            return false;
        }
    }
}
