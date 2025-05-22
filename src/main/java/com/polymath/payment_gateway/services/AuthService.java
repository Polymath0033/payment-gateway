package com.polymath.payment_gateway.services;

import com.polymath.payment_gateway.dto.request.SignupRequest;
import com.polymath.payment_gateway.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public String signUp(SignupRequest request);
}
