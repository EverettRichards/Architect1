package edu.sdccd.cisc191.client.models;

import java.util.List;

public interface DataFetcher {
    String apiEndpointURL = "/api";

    List getAll();

    String getSingle(Long id);

    void delete(Long id);

}
