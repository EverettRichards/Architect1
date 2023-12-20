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

/**
 * UserDataFetcher*
 * Handles all API calls to backend for User data.
 */
public class UserDataFetcher implements DataFetcher {
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches a user by username
     * @param name username of user.
     * @return fetchUser.getBody() returns the User object.
     */
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

    /**
     * Fetches a user by id
     * @param id id of user.
     * @return fetchUser.getBody() returns the User object.
     */
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

    /**
     * Adds a new user.
     * @param user User object to be added.
     * @return success (if successful)
     */
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

        return "Success";
    }

    /**
     * Updates an existing user.
     * @param user User object to be updated.
     * @return success (if successful)
     */
    public static String update(User user) {
        try {
            restTemplate.exchange(
                    DataFetcher.backendEndpointURL + DataFetcher.userEndpointURL + "/update/" + user.getId(),
                    HttpMethod.POST,
                    new HttpEntity<>(user),
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception error) {
            throw new InvalidPayloadException();
        }
        return "Success";
    }

    /**
     * Deletes a user.
     * @param id id of User object to be deleted.
     * @return success (if successful)
     */
    public static String delete(Long id) {
        try {
            restTemplate.exchange(
                    DataFetcher.backendEndpointURL + DataFetcher.userEndpointURL + "/" + id,
                    HttpMethod.DELETE,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception error) {
            throw new InvalidPayloadException();
        }

        return "Success";
    }
}
