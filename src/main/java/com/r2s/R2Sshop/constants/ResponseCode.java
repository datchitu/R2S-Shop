package com.r2s.R2Sshop.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * */
@Getter
public enum ResponseCode {
    SUCCESS(200, "OK", HttpStatus.OK),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    NO_PARAM(6001, "No param", HttpStatus.BAD_REQUEST),
    MISSING_PARAM(6002, "Missing param", HttpStatus.BAD_REQUEST),
    NO_CONTENT(6002, "No content", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(5000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(6003, "User not found", HttpStatus.NOT_FOUND),
    SUBJECT_NOT_FOUND(6004, "Subject not found", HttpStatus.NOT_FOUND),
    INVALID_VALUE(3000, "Invalid value", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(3001, "Invalid parameter", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_EXISTS(2023, "Data already exists", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * Assign the code, message, http status.
     * <p>
     * This function is used to assign the code, message, http status passed in to the code,
     * message and httpStatus above.
     * @param code
     * @param message
     * @param httpStatus
     * @author HoangVu
     * @since 1.0
     */
    private ResponseCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
