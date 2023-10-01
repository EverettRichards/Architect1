package edu.sdccd.cisc191.common.entities;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestsTest {

    @Test
    void get() throws MalformedURLException {
        String result = Requests.get("https://finnhub.io/api/v1/quote?symbol=AAPL&token=bsq5ig8fkcbcavsjbrrg");
        String expectedResult = "{\"c\":171.21,\"d\":0.52,\"dp\":0.3046,\"h\":173.07,\"l\":170.341,\"o\":172.02,\"pc\":170.69,\"t\":1696017601}";
        assertEquals(result, expectedResult);

    }

    @Test
    void post() throws MalformedURLException {


    }

    @Test
    void delete() throws MalformedURLException {
        String url = "http://localhost:8081/your-delete-endpoint";
        Map<String, String> headers = new HashMap<>();
        String body = "";

        try {
            String result = Requests.delete(url, headers, body);

            // Define the expected response for a successful DELETE
            String expectedResponse = "DELETE request was successful.";

            // Compare the entire response
            assertEquals(expectedResponse, result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    void update() throws MalformedURLException {
        String url = "http://localhost:8081/your-update-endpoint"; // Change the URL path
        Map<String, String> headers = new HashMap<>();
        String body = "";

        try {
            // Send a PUT request for the "update" operation
            String result = Requests.update(url, headers, body);

            // Define the expected response for a successful PUT (update)
            String expectedResponse = "UPDATE request was successful.";

            // Compare the entire response
            assertEquals(expectedResponse, result);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}