package com.polymath.payment_gateway.models.enums;

public enum VerificationResult {
    SUCCESS("Email verified successfully"),
    INVALID_TOKEN("Invalid verification token"),
    EXPIRED_TOKEN("Verification token has expired"),
    ALREADY_VERIFIED("Email is already verified");

    private final String message;

    VerificationResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
