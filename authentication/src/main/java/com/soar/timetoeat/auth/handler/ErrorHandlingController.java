package com.soar.timetoeat.auth.handler;

import com.soar.timetoeat.util.faults.ExceptionResponse;
import com.soar.timetoeat.util.faults.auth.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler({IncorrectEmailFormatException.class, PasswordTooSimpleException.class})
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotUniqueException.class)
    public ResponseEntity<ExceptionResponse> handle(EmailNotUniqueException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getEmail()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotUniqueException.class)
    public ResponseEntity<ExceptionResponse> handle(UsernameNotUniqueException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getUsername()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<ExceptionResponse> handle(UnAuthenticatedException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), null),
                HttpStatus.UNAUTHORIZED);
        //yes, yes, UNAUTHORIZED for an UnAuthenticatedException? It's actually a known
        //bizarre problem in HTTP. Unauthorised calls actually use FORBIDDEN status.
    }

    @ExceptionHandler(UnAuthorisedException.class)
    public ResponseEntity<ExceptionResponse> handle(UnAuthorisedException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), null),
                HttpStatus.FORBIDDEN);
    }

}
