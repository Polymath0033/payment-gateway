package com.polymath.payment_gateway.dto.response;

import com.polymath.payment_gateway.models.enums.VerificationResult;

public record EmailVerificationResponse(VerificationResult verificationResult,AuthResponse authResponse) {
}
