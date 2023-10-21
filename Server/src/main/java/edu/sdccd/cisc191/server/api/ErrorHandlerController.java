package edu.sdccd.cisc191.server.api;

import java.nio.file.AccessDeniedException;

import edu.sdccd.cisc191.server.errors.AccessDenied;
import edu.sdccd.cisc191.server.errors.DatabaseError;
import edu.sdccd.cisc191.server.errors.UserNotFound;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ DatabaseError.class })
    public ResponseEntity<Object> handleDatabaseError(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ UserNotFound.class })
    public ResponseEntity<Object> handleUserNotFound(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(ex, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ AccessDenied.class })
    public ResponseEntity<Object> handleAccessDenied(Exception ex, WebRequest request) {
        return new ResponseEntity<Object>(ex, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }
}