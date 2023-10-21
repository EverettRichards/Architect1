package edu.sdccd.cisc191.server.errors;

public class DatabaseError extends Exception {
    public DatabaseError(String errorMessage) {
        super(errorMessage);
    }
}
