package edu.sdccd.cisc191.common.entities;

import java.util.List;

/**
 * DataFetcher is an interface that
 * is used to define class structures
 * that perform REST API CRUD operations.
 */
public interface DataFetcher {

    String backendEndpointURL = "http://localhost:8081";

    String frontendEndpointURL = "http://localhost:8080";

    String apiEndpointURL = "/api";

    String finnhubBaseURL = "https://finnhub.io/api/v1";

    String finnhubKey = "bsq5ig8fkcbcavsjbrrg";

    List getAll();


    void delete(Long id);

}
