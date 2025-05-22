package com.polymath.payment_gateway.exceptions;

public class CustomNotFound extends RuntimeException {
    public CustomNotFound(String message) {
        super(message);
    }
}
