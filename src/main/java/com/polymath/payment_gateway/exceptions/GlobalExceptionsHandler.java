package com.polymath.payment_gateway.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.polymath.payment_gateway.dto.response.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    private final long currentTimestamp = System.currentTimeMillis();

    @ExceptionHandler(CustomNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(CustomNotFound ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(),currentTimestamp);
    }

    @ExceptionHandler(CustomBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(CustomBadRequest ex){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),currentTimestamp);
    }

    @ExceptionHandler(EmailSendingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleEmailSendingException(EmailSendingException ex){
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage(),currentTimestamp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors,currentTimestamp);
    }

}
