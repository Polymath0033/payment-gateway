package com.polymath.payment_gateway.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationRequest(@Email(message = "Invalid email format") @NotBlank(message = "Email cannot be empty") String email) {
}
