package com.polymath.payment_gateway.services;

import com.polymath.payment_gateway.dto.response.EmailVerificationResponse;
import com.polymath.payment_gateway.exceptions.EmailSendingException;
import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.models.enums.VerificationResult;
import org.springframework.stereotype.Service;

@Service
public interface EmailVerificationService {
    public void sendVerificationEmail(Users user) throws EmailSendingException;
    public EmailVerificationResponse verifyEmail(String token);
    public void resendVerificationEmail(String email);
}
