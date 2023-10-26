package edu.sdccd.cisc191.server.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFound extends DatabaseError {
    public UserNotFound(String errorMessage) {
        super(errorMessage);
    }

    public UserNotFound() {
        super("User does not exist in the database.");
    }
}
