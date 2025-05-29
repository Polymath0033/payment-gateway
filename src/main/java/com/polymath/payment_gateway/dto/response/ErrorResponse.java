package com.polymath.payment_gateway.dto.response;


public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    private Object errors;
    public ErrorResponse(int status, String message,long timestamp){
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
    public ErrorResponse(int status, Object errors,long timestamp){
        this.status = status;
        this.errors = errors;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getErrors() {
        return errors;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }
}
