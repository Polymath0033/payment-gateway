package com.polymath.payment_gateway.services.impl;

import com.polymath.payment_gateway.dto.response.AuthResponse;
import com.polymath.payment_gateway.dto.response.EmailVerificationResponse;
import com.polymath.payment_gateway.dto.response.RefreshTokenResponse;
import com.polymath.payment_gateway.dto.response.UserInfo;
import com.polymath.payment_gateway.exceptions.CustomBadRequest;
import com.polymath.payment_gateway.exceptions.CustomNotFound;
import com.polymath.payment_gateway.exceptions.EmailSendingException;
import com.polymath.payment_gateway.models.EmailVerificationToken;
import com.polymath.payment_gateway.models.RefreshToken;
import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.models.enums.VerificationResult;
import com.polymath.payment_gateway.repositories.EmailVerificationTokenRepository;
import com.polymath.payment_gateway.repositories.UserRepository;
import com.polymath.payment_gateway.services.EmailVerificationService;
import com.polymath.payment_gateway.services.JwtService;
import com.polymath.payment_gateway.services.TokenService;
import com.polymath.payment_gateway.utils.TimeToSecondsConverter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    @Value("${jwt.refresh-token-expiration}")
    private int expirationHours;
    @Value("${app.from-email}")
    private String fromEmail;
    @Value("${app.base-url}")
    private String baseUrl;
    private final TokenService tokenService;

    @Override
    public void sendVerificationEmail(Users user)  {
        emailVerificationTokenRepository.deleteByUser(user);
        String token = generateToken();
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(user,token,expirationHours);
        emailVerificationTokenRepository.save(emailVerificationToken);
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            helper.setFrom("no-reply@topay.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Verify Your Email - ToPay");

            String verificationUrl = baseUrl+"/api/auth/verify-email?token="+token;
            Context context = new Context();
            context.setVariable("username",user.getUsername());
            context.setVariable("verificationUrl",verificationUrl);
            context.setVariable("expirationHours",expirationHours/3600);
            String htmlContent = templateEngine.process("email-verification",context);
            helper.setText(htmlContent, true);
            mailSender.send(message);

            log.info("Verification email sent to user: {}", user.getEmail());

        } catch (MessagingException e) {
            log.error("Failed to send verification email to user: {}",user.getEmail(),e);
            throw new EmailSendingException("Email verification failed",e);
        }
    }



    @Override
    public EmailVerificationResponse verifyEmail(String token) {
        Optional<EmailVerificationToken> tokenOpt = emailVerificationTokenRepository.findByToken(token);
        if(tokenOpt.isEmpty()){
            throw new CustomBadRequest(VerificationResult.INVALID_TOKEN.getMessage());
        }
        EmailVerificationToken emailVerificationToken = tokenOpt.get();
        if(emailVerificationToken.isVerified()){
            throw new CustomBadRequest(VerificationResult.ALREADY_VERIFIED.getMessage());
        }
        if(emailVerificationToken.isExpired()){
            emailVerificationTokenRepository.delete(emailVerificationToken);
            throw new CustomBadRequest(VerificationResult.EXPIRED_TOKEN.getMessage());
        }
        emailVerificationToken.setVerifiedAt(LocalDateTime.now());
        emailVerificationTokenRepository.save(emailVerificationToken);
        Users user = emailVerificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        RefreshTokenResponse tokenManagement = tokenService.generateRefreshToken(user.getEmail());
        AuthResponse authResponse = new AuthResponse(tokenManagement,new UserInfo(user.getId(),user.getEmail(),user.getRole()));
        return new EmailVerificationResponse(VerificationResult.SUCCESS,authResponse);
    }

    @Override
    public void resendVerificationEmail(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(()->new CustomNotFound("User with email doesn't exist"));
        if(user.isEmailVerified()){
            throw  new CustomNotFound("User with email already verified");
        }
        sendVerificationEmail(user);

    }

    private String generateToken(){
        SecureRandom random = new SecureRandom();
        byte [] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    @Scheduled(fixedRate=36000000)
    public void cleanExpiredToken(){
        emailVerificationTokenRepository.deleteExpiredTokens(LocalDateTime.now());

    }


}
