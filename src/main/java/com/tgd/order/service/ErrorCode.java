package com.tgd.order.service;

public enum ErrorCode {
    SUCCESS("0000", "SUCCESS"),
    FAILURE("1000", "FAILED");


    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
