package com.polymath.payment_gateway.dto.response;

import com.polymath.payment_gateway.models.enums.Role;

import java.util.UUID;

public record UserInfo(UUID userId, String email, Role role) {
}
