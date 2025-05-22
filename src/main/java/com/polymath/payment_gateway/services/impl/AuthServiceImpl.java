package com.polymath.payment_gateway.services.impl;

import com.polymath.payment_gateway.exceptions.CustomBadRequest;
import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.models.enums.Role;
import com.polymath.payment_gateway.repositories.UserRepository;
import com.polymath.payment_gateway.dto.request.SignupRequest;
import com.polymath.payment_gateway.dto.response.AuthResponse;
import com.polymath.payment_gateway.services.AuthService;
import com.polymath.payment_gateway.services.JwtService;
import com.polymath.payment_gateway.services.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    @Autowired
    AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public String signUp(SignupRequest request) {
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

        return null;
    }
   // @Transactional
//    public void handleTokenGeneration(Users user){
//        Cookie cookie = new Cookie(user);
//    }
}
