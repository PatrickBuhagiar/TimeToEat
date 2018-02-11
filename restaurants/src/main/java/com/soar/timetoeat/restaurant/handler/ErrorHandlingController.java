package com.soar.timetoeat.restaurant.handler;

import com.soar.timetoeat.util.faults.ExceptionResponse;
import com.soar.timetoeat.util.faults.auth.UnAuthenticatedException;
import com.soar.timetoeat.util.faults.auth.UnAuthorisedException;
import com.soar.timetoeat.util.faults.restaurant.RestaurantAddressNotDefinedException;
import com.soar.timetoeat.util.faults.restaurant.RestaurantNameNotDefinedException;
import com.soar.timetoeat.util.faults.restaurant.RestaurantNameNotUnique;
import com.soar.timetoeat.util.faults.restaurant.RestaurantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler(RestaurantNameNotUnique.class)
    public ResponseEntity<ExceptionResponse> handle(RestaurantNameNotUnique e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getRestaurantName()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(RestaurantNotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getRestaurantName()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RestaurantAddressNotDefinedException.class, RestaurantNameNotDefinedException.class})
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
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
