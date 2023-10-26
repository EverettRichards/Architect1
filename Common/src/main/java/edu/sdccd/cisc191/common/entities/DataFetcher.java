package edu.sdccd.cisc191.common.entities;

import java.util.List;

/**
 * DataFetcher is an interface that
 * is used to define class structures
 * that perform REST API CRUD operations.
 */
public interface DataFetcher {

    final static String backendEndpointURL = "http://localhost:8081";

    final static String frontendEndpointURL = "http://localhost:8080";

    final static String apiEndpointURL = "/api";

    final static String userEndpointURL = "/api/user";

    final static String finnhubBaseURL = "https://finnhub.io/api/v1";

    final static String finnhubKey = "bsq5ig8fkcbcavsjbrrg";
}
