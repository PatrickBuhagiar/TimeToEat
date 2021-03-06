package com.soar.timetoeat.menu.handler;

import com.soar.timetoeat.util.faults.ExceptionResponse;
import com.soar.timetoeat.util.faults.auth.UnAuthenticatedException;
import com.soar.timetoeat.util.faults.auth.UnAuthorisedException;
import com.soar.timetoeat.util.faults.menu.MenuNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(MenuNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getRestaurantId()),
                HttpStatus.NOT_FOUND);
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
