package com.r2s.R2Sshop.rest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

/**
 * Export the ErrorRespone information as a JSON file.
 * <p>
 * This function exports the ErrorResponse information, including status, message and timestamp as a JSON file.
 * This helps to initialize an object using the ErrorResponse.builder().status(404)...build() method.
 * @author HoangVu
 * @since 1.0
 */
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    int status;
    String message;
    Timestamp timestamp;
}
