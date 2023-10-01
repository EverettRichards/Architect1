package edu.sdccd.cisc191.client.models;

import java.util.List;

/**
 * DataFetcher is an interface that handles the api calls
 */
public interface DataFetcher {
    String apiEndpointURL = "/api";

    List getAll();


    void delete(Long id);

}
