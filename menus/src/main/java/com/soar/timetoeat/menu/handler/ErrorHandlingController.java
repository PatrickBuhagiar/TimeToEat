package com.soar.timetoeat.menu.handler;

import com.soar.timetoeat.util.exceptions.ExceptionResponse;
import com.soar.timetoeat.util.exceptions.menu.MenuNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(MenuNotFound.class)
    public ResponseEntity<ExceptionResponse> handle(MenuNotFound e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getRestaurantId()),
                HttpStatus.NOT_FOUND);
    }
}
