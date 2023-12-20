package edu.sdccd.cisc191.client.models;

import edu.sdccd.cisc191.common.entities.DataFetcher;
import edu.sdccd.cisc191.common.entities.Stock;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * StockDataFetcher*
 * Handles all API calls to backend for Stock data.
 */
public class StockDataFetcher implements DataFetcher {
    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches a single stock by ticker symbol.
     * @param ticker ticker symbol of stock to be found.
     * @return
     */
    public static Stock get(String ticker) {
        Stock stock;
        try {
            ResponseEntity<Stock> response = restTemplate.exchange(
                    DataFetcher.backendEndpointURL + DataFetcher.apiEndpointURL + "/stock/" + ticker,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            stock = response.getBody();
        } catch (Exception error){
            throw new EntityNotFoundException();
        }

        return stock;
    }

}
