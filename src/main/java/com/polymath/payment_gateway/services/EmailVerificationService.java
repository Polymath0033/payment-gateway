package com.polymath.payment_gateway.services;

import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.models.enums.VerificationResult;
import org.springframework.stereotype.Service;

@Service
public interface EmailVerificationService {
    public void sendVerificationEmail(Users user);
    public VerificationResult verifyEmail(String token);
    public void resendVerificationEmail(String email);
}
