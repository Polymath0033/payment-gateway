package com.polymath.payment_gateway.dto.response;

public record RefreshTokenResponse(String accessToken,String refreshToken,Long accessTokenExpiresAt,Long refreshTokenExpiresAt,Boolean revoked) {
}
