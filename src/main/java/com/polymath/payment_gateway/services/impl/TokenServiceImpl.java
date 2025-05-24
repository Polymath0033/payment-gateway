package com.polymath.payment_gateway.services.impl;

import com.polymath.payment_gateway.dto.response.RefreshTokenResponse;
import com.polymath.payment_gateway.exceptions.CustomBadRequest;
import com.polymath.payment_gateway.exceptions.CustomNotFound;
import com.polymath.payment_gateway.models.RefreshToken;
import com.polymath.payment_gateway.models.Users;
import com.polymath.payment_gateway.repositories.RefreshTokenRepository;
import com.polymath.payment_gateway.repositories.UserRepository;
import com.polymath.payment_gateway.services.JwtService;
import com.polymath.payment_gateway.services.TokenService;
import com.polymath.payment_gateway.utils.TimeToSecondsConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public TokenServiceImpl(JwtService jwtService, RefreshTokenRepository refreshTokenRepository,UserRepository userRepository){
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveToken(String token, Users user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setIssuedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        Optional<RefreshToken> tokens = refreshTokenRepository.findByToken(refreshToken);
        return tokens.isPresent()&&!tokens.get().isRevoked()&&tokens.get().getExpiresAt().isAfter(LocalDateTime.now());
    }

    @Override
    public void revokeAndDeleteTokenForUser(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);

    }

    @Override
    public RefreshTokenResponse getRefreshToken(String accessToken) {
        String email = jwtService.extractEmail(accessToken);
        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(email).orElseThrow(()-> new CustomNotFound("Email not found"));
        if(!isRefreshTokenValid(refreshToken.getToken())){
            throw new CustomBadRequest("Invalid refresh token");
        }
        LocalDateTime accessTokenExpirationTime = jwtService.expirationDate(accessToken);
        LocalDateTime refreshTokenExpirationTime = jwtService.expirationDate(refreshToken.getToken());
        return new RefreshTokenResponse(accessToken, refreshToken.getToken(), TimeToSecondsConverter.convertDateTimeToSeconds(accessTokenExpirationTime),TimeToSecondsConverter.convertDateTimeToSeconds(refreshTokenExpirationTime),refreshToken.isRevoked());
    }

    @Override
    public RefreshTokenResponse generateRefreshToken(String email) {
        if(email == null || email.isEmpty()){
            throw new CustomBadRequest("Email must not be empty");
        }
        Users user = userRepository.findByEmail(email).orElseThrow(()->new CustomNotFound("User with email"+email+" not found"));
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        LocalDateTime accessTokenExpiresTime = jwtService.expirationDate(accessToken);
        LocalDateTime refreshTokenExpiresTime = jwtService.expirationDate(refreshToken);
        if(refreshTokenRepository.existsByUserId(user.getId())){
            RefreshToken existingRefreshToken = refreshTokenRepository.findByUserEmail(email).orElseThrow(()->new CustomNotFound("User with email"+email+" not found"));
            existingRefreshToken.setToken(refreshToken);
            existingRefreshToken.setIssuedAt(LocalDateTime.now());
            existingRefreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));
            existingRefreshToken.setRevoked(false);
            refreshTokenRepository.save(existingRefreshToken);
        }else{
            saveToken(accessToken, user);
        }
        return new RefreshTokenResponse(accessToken,refreshToken, TimeToSecondsConverter.convertDateTimeToSeconds(accessTokenExpiresTime),TimeToSecondsConverter.convertDateTimeToSeconds(refreshTokenExpiresTime),false);
    }

}
