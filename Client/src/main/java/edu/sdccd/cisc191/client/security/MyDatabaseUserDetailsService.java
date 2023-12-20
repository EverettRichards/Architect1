package edu.sdccd.cisc191.client.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static edu.sdccd.cisc191.common.entities.DataFetcher.backendEndpointURL;
import static edu.sdccd.cisc191.common.entities.DataFetcher.userEndpointURL;
import edu.sdccd.cisc191.common.entities.User;

import java.util.ArrayList;
import java.util.Collection;

public class MyDatabaseUserDetailsService implements UserDetailsService {
    private final String baseURL = backendEndpointURL + userEndpointURL;
    private RestTemplate restTemplate = new RestTemplate();

    //For user authorization role.
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // (1)
        // 1. Load the user from the users table by username. If not found, throw UsernameNotFoundException.
        // 2. Convert/wrap the user to a UserDetails object and return it.
        ResponseEntity<String> response;
        User user;
        UserDetails authUser;
        String userRole = "USER";


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
        }
        catch(ClassCastException e) {
            throw new InvalidPayloadException("Invalid payload.");
        } catch(RestClientException e) {
            throw new UsernameNotFoundException("Client Error.");
        }
        try {
            user = new ObjectMapper().readValue(response.getBody(),
                    User.class);
        } catch(JsonProcessingException e) {
            throw new UsernameNotFoundException("User not found.");
        }

        if (user.getRoleAsString() == "ROLE_ADMIN") {
            userRole = "ADMIN";
        }

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        authUser = AuthUser.withUsername(user.getName())
                .password(encoder.encode(user.getPasswordHash()))
                .roles(userRole)
                .build();

        return authUser;
    }
}
