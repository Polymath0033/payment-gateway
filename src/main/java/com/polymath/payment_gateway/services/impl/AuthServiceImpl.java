package com.polymath.payment_gateway.services.impl;

import com.polymath.payment_gateway.dto.request.LoginRequest;
import com.polymath.payment_gateway.dto.response.RefreshTokenResponse;
import com.polymath.payment_gateway.dto.response.UserInfo;
import com.polymath.payment_gateway.exceptions.CustomBadRequest;
import com.polymath.payment_gateway.exceptions.CustomNotFound;
import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.repositories.UserRepository;
import com.polymath.payment_gateway.dto.request.SignupRequest;
import com.polymath.payment_gateway.dto.response.AuthResponse;
import com.polymath.payment_gateway.services.AuthService;
import com.polymath.payment_gateway.services.EmailVerificationService;
import com.polymath.payment_gateway.services.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRepository userRepository;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;


    public AuthServiceImpl(UserRepository userRepository, TokenService tokenService, AuthenticationManager authenticationManager, EmailVerificationService emailVerificationService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.emailVerificationService = emailVerificationService;
    }

    @Override
    @Transactional
    public void signUp(SignupRequest request) {
        System.out.println("Hello from sign up");
        Optional<Users> existingUserWithEmail = userRepository.findByEmail(request.email());
        Optional<Users> existingUserWithUsername = userRepository.findByUsername(request.username());
        if(existingUserWithEmail.isPresent()) {
            throw new CustomBadRequest("Email already exists");
        }
        if(existingUserWithUsername.isPresent()){
            throw new CustomBadRequest("Username already exist");
        }
       Users user = new Users();
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(request.role());
        user.setActive(true);
        user.setEmailVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        emailVerificationService.sendVerificationEmail(user);

    }


    @Transactional
    @Override
    public AuthResponse logIn(LoginRequest loginRequest){
         userRepository.findByEmail(loginRequest.email()).orElseThrow(()->new CustomNotFound("This email doesn't exist"));
        Users existingUserVerification = userRepository.findByEmailAndActiveTrueAndEmailVerifiedTrue(loginRequest.email()).orElseThrow(()->new CustomNotFound("Verify your emai and try and logging again"));
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
            if(authentication.isAuthenticated()){
               RefreshTokenResponse refreshToken = tokenService.generateRefreshToken(loginRequest.email());
               return new AuthResponse(refreshToken,new UserInfo(existingUserVerification.getId(),existingUserVerification.getEmail(),existingUserVerification.getRole()));
            }else{
                return new AuthResponse(null,null);
            }
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }

   // @Transactional
//    public void handleTokenGeneration(Users user){
//        Cookie cookie = new Cookie(user);
//    }
}
