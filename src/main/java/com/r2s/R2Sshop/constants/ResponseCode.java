package com.r2s.R2Sshop.constants;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * */
@Getter
public enum ResponseCode {
    FAILURE_SIGNIN(400, "The username or password does not match", HttpStatus.UNAUTHORIZED),
    IS_LOCKED(401, "The account has been looked", HttpStatus.FORBIDDEN),
    IS_LOCKED_PERMANENT(402, "The account has been permanently looked", HttpStatus.FORBIDDEN),
    IS_DELETED(403, "The account has been deleted", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    ADDRESS_NOT_FOUND(405, "Address Not found", HttpStatus.NOT_FOUND),
    ACCESS_DENIED(406, "Access denied", HttpStatus.UNAUTHORIZED),
    MISSING_PARAM(6002, "Missing param", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(6004, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(6005, "Role not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(6006, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(6007, "Product not found", HttpStatus.NOT_FOUND),
    VARIANT_PRODUCT_NOT_FOUND(6008, "Variant product not found", HttpStatus.NOT_FOUND),
    VOUCHER_NOT_FOUND(6009, "Voucher not found", HttpStatus.NOT_FOUND),
    USERVOUCHER_NOT_FOUND(6010, "UserVoucher not found", HttpStatus.NOT_FOUND),
    CART_LINE_ITEM_NOT_FOUND(6011, "CartLineItem not found", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(6012, "Cart not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(6013, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND(6014, "Order item not found", HttpStatus.NOT_FOUND),
    INVALID_VALUE(3001, "Invalid value", HttpStatus.BAD_REQUEST),
    INVALID_PARAM(3002, "Invalid parameter", HttpStatus.BAD_REQUEST),
    NOT_MATCH_PASSWORD(3003, "Password does not match", HttpStatus.BAD_REQUEST),
    DUPLICATE_PASSWORD(3004, "Duplicate old password", HttpStatus.BAD_REQUEST),
    REQUEST_IS_CONFLICT(3006, "The request is in conflict", HttpStatus.CONFLICT),
    INSUFFICIENT_STOCK(3011, "Insufficient stock", HttpStatus.BAD_REQUEST),
    IMMUTABLE(3007, "The value remains unchanged", HttpStatus.BAD_REQUEST),
    DATA_ALREADY_EXISTS(2023, "Data already exists", HttpStatus.BAD_REQUEST),
    VARIANT_PRODUCT_ALREADY_DELETED(2024, "Variant product already been deleted",
            HttpStatus.BAD_REQUEST),
    VARIANT_PRODUCT_ALREADY_RESTORED(2025, "Variant product already been restored",
            HttpStatus.BAD_REQUEST),
    DATA_ALREADY_DEFAULTED(2026, "The data has been set to default", HttpStatus.BAD_REQUEST),
    USER_ALREADY_DELETED(2027, "User already been deleted", HttpStatus.BAD_REQUEST),
    USER_ALREADY_RESTORED(2028, "User already been restored", HttpStatus.BAD_REQUEST),
    USER_ALREADY_LOCKED(2029, "User already been locked", HttpStatus.BAD_REQUEST),
    USER_ALREADY_UNLOCKED(2030, "User already been unlocked", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_EXISTS(2031, "Product already exists", HttpStatus.BAD_REQUEST),
    VARIANT_PRODUCT_ALREADY_EXISTS(2032, "Variant product already exists", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_EXISTS(2021, "Voucher already exists", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_DELETED(2033, "Voucher already been deleted", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_RESTORED(2034, "UserVoucher already been restored", HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_DELETED(2035, "UserVoucher already been deleted", HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_RESTORED(2036, "UserVoucher already been restored", HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_NOT_YET_RELEASED(2037, "UserVoucher already been not yet released",
            HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_RELEASED(2038, "UserVoucher already been released",
            HttpStatus.BAD_REQUEST),
    USERVOUCHER_ALREADY_USED(2039, "UserVoucher already been used",
            HttpStatus.BAD_REQUEST),
    CART_LINE_ITEM_ALREADY_DELETED(2040, "CartLineItem already been deleted",
            HttpStatus.BAD_REQUEST),
    CART_ALREADY_PAID(2041, "Cart already been paid", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_DELETED(2042, "Order already been deleted", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_RESTORED(2043, "Order already been restored", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_CANCELED(2044, "Order already been canceled", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_REACTIVATED(2045, "Order already been reactivated", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_DELIVERED(2046, "Order already been delivered", HttpStatus.BAD_REQUEST),
    CART_ALREADY_ORDERED(2047, "Cart already been ordered", HttpStatus.BAD_REQUEST),
    CART_IS_EMPTY(2047, "Cart is empty", HttpStatus.BAD_REQUEST),
    CART_NOT_YET_PAID(2048, "Cart is not yet pair for", HttpStatus.BAD_REQUEST),
    EXPIRE_DATE_IS_AFTER(2049, "ExpireDate is after voucher expireDate", HttpStatus.BAD_REQUEST),
    UNISSUED_VOUCHER(2050, "Unissued voucher ", HttpStatus.BAD_REQUEST),
    VOUCHER_ALREADY_USED(2051, "Voucher already been used",
            HttpStatus.BAD_REQUEST),
    OUT_OF_STOCK_VOUCHER(2052, "Out of stock voucher", HttpStatus.BAD_REQUEST),
    ADDRESS_ALREADY_RESTORED(2053, "Address already been restored", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_DELETED(2054, "Category already been deleted", HttpStatus.BAD_REQUEST),
    CATEGORY_ALREADY_RESTORED(2055, "Category already been restored", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_DELETED(2056, "Product already been deleted", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_RESTORED(2057, "Product already been restored", HttpStatus.BAD_REQUEST),
    USER_ALREADY_LOCKED_PERMANENT(2058, "User already been permanently locked",
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
