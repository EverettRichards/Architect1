package edu.sdccd.cisc191.client.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UsernameTakenException extends FrontendException {
    public UsernameTakenException(String errorMessage) {
        super(errorMessage);
    }

    public UsernameTakenException() {
        super("Username has already been taken. Use a different username.");
    }
}
