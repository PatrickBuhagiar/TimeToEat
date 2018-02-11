package com.soar.timetoeat.order.handler;

import com.soar.timetoeat.util.faults.ExceptionResponse;
import com.soar.timetoeat.util.faults.auth.UnAuthenticatedException;
import com.soar.timetoeat.util.faults.auth.UnAuthorisedException;
import com.soar.timetoeat.util.faults.order.DeliveryAddressNotDefinedException;
import com.soar.timetoeat.util.faults.order.EmptyOrderException;
import com.soar.timetoeat.util.faults.order.InvalidPaymentDetailsException;
import com.soar.timetoeat.util.faults.order.OrderDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingController {

    @ExceptionHandler({DeliveryAddressNotDefinedException.class, EmptyOrderException.class, InvalidPaymentDetailsException.class})
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderDoesNotExistException.class)
    public ResponseEntity<ExceptionResponse> handle(OrderDoesNotExistException e) {
        return new ResponseEntity<>(new ExceptionResponse(e.getMessage(), e.getOrderId()),
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
