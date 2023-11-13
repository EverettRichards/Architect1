package edu.sdccd.cisc191.server.errors;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class BackendException extends RuntimeException {
    public BackendException(String errorMessage) {
        super(errorMessage);
    }
}
