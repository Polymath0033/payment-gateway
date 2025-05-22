package com.polymath.payment_gateway.services;

import com.polymath.payment_gateway.dto.response.RefreshTokenResponse;
import com.polymath.payment_gateway.models.Users;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TokenService {
    public void saveToken(String refreshToken, Users user);
    public boolean isRefreshTokenValid(String refreshToken);
    public void revokeAndDeleteTokenForUser(UUID userId);
    public RefreshTokenResponse getRefreshToken(String accessToken);
    public RefreshTokenResponse generateRefreshToken(String email);
}
