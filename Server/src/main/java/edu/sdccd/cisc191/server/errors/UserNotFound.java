package edu.sdccd.cisc191.server.errors;

public class UserNotFound extends DatabaseError {
    public UserNotFound(String errorMessage) {
        super(errorMessage);
    }

    public UserNotFound() {
        super("User does not exist in the database.");
    }
}
