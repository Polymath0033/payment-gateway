package com.polymath.payment_gateway.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message,long timestamp) {
}
