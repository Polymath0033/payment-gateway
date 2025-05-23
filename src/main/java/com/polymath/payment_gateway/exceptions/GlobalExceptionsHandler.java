package com.polymath.payment_gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.polymath.payment_gateway.dto.response.ErrorResponse;
@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(CustomNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(CustomNotFound ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(),System.currentTimeMillis());
    }

    @ExceptionHandler(CustomBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(CustomBadRequest ex){
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(),System.currentTimeMillis());
    }
}
