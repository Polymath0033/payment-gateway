package com.polymath.payment_gateway.services;

import com.polymath.payment_gateway.dto.request.LoginRequest;
import com.polymath.payment_gateway.dto.request.SignupRequest;
import com.polymath.payment_gateway.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public void signUp(SignupRequest request);
    public AuthResponse logIn(LoginRequest loginRequest);
}
