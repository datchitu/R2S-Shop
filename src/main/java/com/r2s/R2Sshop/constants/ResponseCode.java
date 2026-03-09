package com.r2s.R2Sshop.constants;

public enum ResponseCode {
    SUCCESS(200, "OK"),
    NOT_FOUND(404, "Not found"),
    NO_PARAM(6001, "No param"),
    NO_CONTENT(2004, "No content"),
    INTERNAL_SERVER_ERROR(5000, "Internal server error"),
    USER_NOT_FOUND(4005, "User not found"),
    SUBJECT_NOT_FOUND(4006, "Subject not found"),
    INVALID_VALUE(3000, "Invalid value"),
    DATA_ALREADY_EXISTS(2023, "Data already exists");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
