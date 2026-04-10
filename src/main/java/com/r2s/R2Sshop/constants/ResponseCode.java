package com.r2s.R2Sshop.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * */
@Getter
public enum ResponseCode {
    SUCCESS(200, "OK", HttpStatus.OK),
    ACCESS_DENIED(403, "Access denied", HttpStatus.UNAUTHORIZED),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_FOUND(405, "Address Not found", HttpStatus.NOT_FOUND),
    NO_PARAM(6001, "No param", HttpStatus.BAD_REQUEST),
    MISSING_PARAM(6002, "Missing param", HttpStatus.BAD_REQUEST),
    NO_CONTENT(6003, "No content", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(5000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(6004, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(6005, "Role not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(6006, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(6007, "Product not found", HttpStatus.NOT_FOUND),
    INVALID_VALUE(3001, "Invalid value", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(3002, "Invalid parameter", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_EXISTS(2023, "Data already exists", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_DELETED(2024, "Data already been deleted", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_REACTIVATED(2025, "Data already been reactivated", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_EXISTS(2026, "Product already exists", HttpStatus.BAD_REQUEST),
    INSERT_FAILURE(4001, "Insert failure", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILURE_SIGNIN(3000, "Failed sign in", HttpStatus.UNAUTHORIZED),
    FAILURE_USER_UPDATE(3001, "User update failed", HttpStatus.CONFLICT),
    NOT_MATCH_PASSWORD(3002, "Password does not match", HttpStatus.BAD_REQUEST),
    DUPLICATE_PASSWORD(3003, "Duplicate old password", HttpStatus.BAD_REQUEST),
    FAILURE_PASSWORD_CHARGE(3004, "Password charging failed", HttpStatus.CONFLICT),
    FAILURE_ADDRESS_UPDATE(3005, "Address update failed", HttpStatus.CONFLICT),
    FAILURE_ADDRESS_DELETE(3006, "Address deletion failed", HttpStatus.CONFLICT),
    FAILURE_ADDRESS_REACTIVATE(3007, "Address reactivation failed", HttpStatus.CONFLICT),
    FAILURE_CATEGORY_UPDATE(3008, "Category update failed", HttpStatus.CONFLICT),
    FAILURE_CATEGORY_DELETE(3009, "Category deletion failed", HttpStatus.CONFLICT),
    FAILURE_CATEGORY_REACTIVATE(3010, "Category reactivation failed", HttpStatus.CONFLICT),
    FAILURE_PRODUCT_UPDATE(3011, "Product update failed", HttpStatus.CONFLICT),
    FAILURE_PRODUCT_DELETE(3012, "Product deletion failed", HttpStatus.CONFLICT),
    FAILURE_PRODUCT_REACTIVATE(3013, "Product reactivation failed", HttpStatus.CONFLICT),
    AUTHENTICATION_ERROR(3033, "Failed sign in", HttpStatus.UNAUTHORIZED);

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
