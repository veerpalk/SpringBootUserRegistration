package com.adsologist.adsologist.handler;

import com.adsologist.adsologist.exceptions.UserCreationException;
import com.adsologist.adsologist.exceptions.UserAlreadyExistsException;
import com.adsologist.adsologist.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<Object>  handleException(UserCreationException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
       return buildResponseEntity(new ErrorResponse(HttpStatus.NO_CONTENT, ex.getMessage(), ex));

    }
    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildResponseEntity(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex));

    }

    private ResponseEntity<Object> buildResponseEntity(ErrorResponse apiError) {
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }
}
