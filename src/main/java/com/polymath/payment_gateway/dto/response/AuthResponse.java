package com.polymath.payment_gateway.dto.response;

public record AuthResponse(String accessToken, String refreshToken,UserInfo user) {
}
