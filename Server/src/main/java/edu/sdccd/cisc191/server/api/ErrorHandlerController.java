package edu.sdccd.cisc191.server.api;

import edu.sdccd.cisc191.server.errors.AccessDenied;
import edu.sdccd.cisc191.server.errors.BackendException;
import edu.sdccd.cisc191.server.errors.DatabaseError;
import edu.sdccd.cisc191.server.errors.UserExists;
import edu.sdccd.cisc191.server.errors.UserNotFound;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ DatabaseError.class })
    public BackendException handleDatabaseError(DatabaseError ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ UserExists.class })
    public UserExists handleUserExists(UserExists ex, WebRequest request) {
    //     return new ResponseEntity<Object>(ex, new HttpHeaders(), HttpStatus.NOT_FOUND);
        return ex;
    }

    @ExceptionHandler({ UserNotFound.class })
    public BackendException handleUserNotFound(UserNotFound ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ AccessDenied.class })
    public BackendException handleAccessDenied(AccessDenied ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ BackendException.class })
    public BackendException handleBackendExpection(BackendException ex, WebRequest request) {
        return ex;
    }

    // @ExceptionHandler({ HttpMessageNotReadableException.class })
    // public ResponseEntity<Object> handleHttpMessageNotReadableException(Exception ex, WebRequest request) {
    //     return new ResponseEntity<Object>(ex.toString(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    // }
}