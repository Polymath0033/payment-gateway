package com.polymath.payment_gateway.exceptions;

import jakarta.mail.MessagingException;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
