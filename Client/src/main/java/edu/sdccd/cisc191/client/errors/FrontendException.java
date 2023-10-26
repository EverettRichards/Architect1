package edu.sdccd.cisc191.client.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FrontendException extends RuntimeException {
    public FrontendException(String errorMessage) {
        super(errorMessage);
    }
}
