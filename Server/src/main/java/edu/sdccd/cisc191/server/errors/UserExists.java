package edu.sdccd.cisc191.server.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserExists extends RuntimeException {
    public UserExists(String errorMessage) {
        super(errorMessage);
    }

    public UserExists() {
        super("Username already taken");
    }
}
