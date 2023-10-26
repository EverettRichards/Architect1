package edu.sdccd.cisc191.server.api;

import java.nio.file.AccessDeniedException;

import edu.sdccd.cisc191.server.errors.AccessDenied;
import edu.sdccd.cisc191.server.errors.BackendExpection;
import edu.sdccd.cisc191.server.errors.DatabaseError;
import edu.sdccd.cisc191.server.errors.PermissionException;
import edu.sdccd.cisc191.server.errors.UserExists;
import edu.sdccd.cisc191.server.errors.UserNotFound;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ DatabaseError.class })
    public BackendExpection handleDatabaseError(DatabaseError ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ UserExists.class })
    public UserExists handleUserExists(UserExists ex, WebRequest request) {
    //     return new ResponseEntity<Object>(ex, new HttpHeaders(), HttpStatus.NOT_FOUND);
        return ex;
    }

    @ExceptionHandler({ UserNotFound.class })
    public BackendExpection handleUserNotFound(UserNotFound ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ AccessDenied.class })
    public BackendExpection handleAccessDenied(AccessDenied ex, WebRequest request) {
        return ex;
    }

    @ExceptionHandler({ BackendExpection.class })
    public BackendExpection handleBackendExpection(BackendExpection ex, WebRequest request) {
        return ex;
    }

    // @ExceptionHandler({ HttpMessageNotReadableException.class })
    // public ResponseEntity<Object> handleHttpMessageNotReadableException(Exception ex, WebRequest request) {
    //     return new ResponseEntity<Object>(ex.toString(), new HttpHeaders(), HttpStatus.FORBIDDEN);
    // }
}