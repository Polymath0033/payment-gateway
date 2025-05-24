package com.polymath.payment_gateway.dto.response;

public record AuthResponse(RefreshTokenResponse tokens,UserInfo user) {
}
