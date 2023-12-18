package edu.sdccd.cisc191.client.models;

import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.client.errors.UsernameTakenException;
import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class UserDataFetcher implements DataFetcher {
    private static RestTemplate restTemplate = new RestTemplate();

    //Fetch user by username
    public static User get(String name) {
        ResponseEntity<User> fetchUser;
        try {
            fetchUser = restTemplate.exchange(
                    DataFetcher.backendEndpointURL + DataFetcher.userEndpointURL + "/name/" + name,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception error) {
            throw new InvalidPayloadException();
        }

        return fetchUser.getBody();
    }

    //Fetch user by id
    public static User get(Long id) {
        ResponseEntity<User> fetchUser;
        try {
            fetchUser = restTemplate.exchange(
                    DataFetcher.backendEndpointURL + DataFetcher.userEndpointURL + "/" + id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception error) {
            throw new InvalidPayloadException();
        }

        return fetchUser.getBody();
    }

    public static String add(User user) {
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    DataFetcher.backendEndpointURL + DataFetcher.userEndpointURL + "/add",
                    HttpMethod.POST,
                    new HttpEntity<User>(user),
                    new ParameterizedTypeReference<>() {}
            );

            if(response.getStatusCode().is4xxClientError()) {
                throw new UsernameTakenException();
            }
        } catch (Exception error) {
            throw new InvalidPayloadException();
        }

        return "Success!";
    }
}
