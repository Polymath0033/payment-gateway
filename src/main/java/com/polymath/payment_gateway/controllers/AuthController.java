package com.polymath.payment_gateway.controllers;

import com.polymath.payment_gateway.dto.request.LoginRequest;
import com.polymath.payment_gateway.dto.request.SignupRequest;
import com.polymath.payment_gateway.dto.response.ApiResponse;
import com.polymath.payment_gateway.dto.response.AuthResponse;
import com.polymath.payment_gateway.dto.response.EmailVerificationResponse;
import com.polymath.payment_gateway.services.AuthService;
import com.polymath.payment_gateway.services.EmailVerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;

    public AuthController(EmailVerificationService emailVerificationService, AuthService authService) {
        this.emailVerificationService = emailVerificationService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
        authService.signUp(request);
        return ApiResponse.handleApiErrorResponse("", HttpStatus.CREATED,"register");
    }
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
            EmailVerificationResponse response = emailVerificationService.verifyEmail(token);
            return ApiResponse.handleApiErrorResponse(response, HttpStatus.OK,"verifyEmail");
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody String email) {
        emailVerificationService.resendVerificationEmail(email);
        return ApiResponse.handleApiErrorResponse("",HttpStatus.OK,"resendVerificationEmail");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
       AuthResponse response = authService.logIn(request);
       return ApiResponse.handleApiErrorResponse(response,HttpStatus.OK,"loginUser");
    }
}
