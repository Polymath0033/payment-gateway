package com.polymath.payment_gateway.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.LinkedHashMap;
import java.util.Map;

public class ApiResponse {
    public static ResponseEntity<Object> handleApiErrorResponse (Object response, HttpStatus status, String message) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", status.value());
        responseMap.put("message", message);
        if(response != null) responseMap.put("data", response);
        return new ResponseEntity<>(responseMap, status);
    }
}
