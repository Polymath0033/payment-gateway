package com.polymath.payment_gateway.services.impl;

import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.models.enums.VerificationResult;
import com.polymath.payment_gateway.repositories.EmailVerificationTokenRepository;
import com.polymath.payment_gateway.repositories.UserRepository;
import com.polymath.payment_gateway.services.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailVerificationImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    @Override
    public void sendVerificationEmail(Users user) {

    }

    @Override
    public VerificationResult verifyEmail(String token) {
        return null;
    }

    @Override
    public void resendVerificationEmail(String email) {

    }
}
