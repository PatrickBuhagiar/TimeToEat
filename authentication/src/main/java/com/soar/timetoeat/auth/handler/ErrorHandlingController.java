package com.soar.timetoeat.auth.handler;

import com.soar.timetoeat.util.exceptions.ExceptionResponse;
import com.soar.timetoeat.util.exceptions.auth.IncorrectEmailFormatException;
import com.soar.timetoeat.util.exceptions.auth.PasswordTooSimpleException;
import com.soar.timetoeat.util.exceptions.auth.UsernameAlreadyExistsException;
import com.soar.timetoeat.util.exceptions.menu.MenuNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler({IncorrectEmailFormatException.class, PasswordTooSimpleException.class})
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handle(UsernameAlreadyExistsException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getUsername()),
                HttpStatus.CONFLICT);
    }

}
