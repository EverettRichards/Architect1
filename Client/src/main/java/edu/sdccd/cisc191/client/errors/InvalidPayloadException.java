package edu.sdccd.cisc191.client.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidPayloadException extends FrontendException {
    public InvalidPayloadException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidPayloadException() {
        super("Not acceptable payload");
    }
}
