package edu.sdccd.cisc191.client.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.sdccd.cisc191.client.errors.InvalidPayloadException;
import edu.sdccd.cisc191.client.errors.UsernameTakenException;

@ControllerAdvice
public class ErrorHandlingController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ InvalidPayloadException.class })
    public InvalidPayloadException handleDatabaseError(InvalidPayloadException ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ UsernameTakenException.class })
    public UsernameTakenException handleUsernameTakenException(UsernameTakenException ex, WebRequest request) {
        return ex;
    }
}