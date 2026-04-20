package com.r2s.R2Sshop.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * */
@Getter
public enum ResponseCode {
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
    VARIANT_PRODUCT_NOT_FOUND(6008, "Variant product not found", HttpStatus.NOT_FOUND),
    VOUCHER_NOT_FOUND(6009, "Voucher not found", HttpStatus.NOT_FOUND),
    USERVOUCHER_NOT_FOUND(6010, "UserVoucher not found", HttpStatus.NOT_FOUND),
    CART_LINE_ITEM_NOT_FOUND(6011, "CartLineItem not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(6012, "Cart not found", HttpStatus.NOT_FOUND),
    INVALID_VALUE(3001, "Invalid value", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(3002, "Invalid parameter", HttpStatus.BAD_REQUEST),
    FAILURE_SIGNIN(3000, "Failed sign in", HttpStatus.UNAUTHORIZED),
    NOT_MATCH_PASSWORD(3003, "Password does not match", HttpStatus.BAD_REQUEST),
    DUPLICATE_PASSWORD(3004, "Duplicate old password", HttpStatus.BAD_REQUEST),
    FAILURE_CATEGORY_UPDATE(3005, "Category update failed", HttpStatus.CONFLICT),
    FAILURE_PRODUCT_UPDATE(3006, "Product update failed", HttpStatus.CONFLICT),
    AUTHENTICATION_ERROR(3010, "Failed sign in", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_STOCK(3011, "Insufficient stock", HttpStatus.BAD_REQUEST),
    IMMUTABLE(3007, "The value remains unchanged", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_EXISTS(2023, "Data already exists", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_DELETED(2024, "Data already been deleted", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_REACTIVATED(2025, "Data already been reactivated", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_DEFAULTED(2026, "The data has been set to default", HttpStatus.BAD_REQUEST),
    USER_ALREADY_DELETED(2027, "User already been deleted", HttpStatus.BAD_REQUEST),
    USER_ALREADY_REACTIVATED(2028, "User already been reactivated", HttpStatus.BAD_REQUEST),
    USER_ALREADY_BLOCKED(2029, "User already been blocked", HttpStatus.BAD_REQUEST),
    USER_ALREADY_UNBLOCKED(2030, "User already been unblocked", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_EXISTS(2031, "Product already exists", HttpStatus.BAD_REQUEST),
    VARIANT_PRODUCT_ALREADY_EXISTS(2032, "Variant product already exists", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_EXISTS(2021, "Voucher already exists", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_DELETED(2033, "Voucher already been deleted", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_REACTIVATED(2034, "UserVoucher already been reactivated", HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_DELETED(2035, "UserVoucher already been deleted", HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_REACTIVATED(2036, "UserVoucher already been reactivated", HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_NOT_RELEASED(2037, "UserVoucher already been not yet released",
            HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_RELEASED(2038, "UserVoucher already been released",
            HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_USED(2039, "UserVoucher already been used",
            HttpStatus.BAD_REQUEST),
    CART_LINE_ITEM_ALREADY_DELETED(2040, "CartLineItem already been deleted",
            HttpStatus.BAD_REQUEST),
    INSERT_FAILURE(4001, "Insert failure", HttpStatus.INTERNAL_SERVER_ERROR);


    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    /**
     * Assign the code, message, http status.
     * <p>
     * This method is used to assign the code, message, http status passed in to the code,
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
