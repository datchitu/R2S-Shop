package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.constants.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BaseRestController{
    /**
     * Return data and HttpStatus.OK.
     * <p>
     * This function reports the status HttpStatus.OK and returns data as a JSON file
     * if the function retrieves the data successfully.
     * @param data
     * @return ResponseEntity<Map<String, Object>>(response, HttpStatus.OK)
     * @author HoangVu
     * @since 1.0
     */
    public ResponseEntity<Map<String, Object>> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "OK");
        response.put("data", data);
        response.put("timestamp",System.currentTimeMillis());
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    /**
     * Build a generic ErrorResponse.
     * <p>
     * This function is used generally to build your response using the Builder method.
     * returns the information including.
     * @param responseCode
     * @return buildErrorResponse(responseCode)
     * @author HoangVu
     * @since 1.0
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(ResponseCode responseCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(responseCode.getCode())
                .message(responseCode.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(errorResponse, responseCode.getHttpStatus());
    }
    /**
     * Configure GlobalExceptionHandler with Builder.
     * <p>
     * This function configures the GlobalExceptionHandler with Builder and then reports the status HttpStatus error,
     * returns the information including.
     * code, message and timestamp if an exception occurs.
     * @param appException
     * @return buildErrorResponse(responseCode)
     * @author HoangVu
     * @since 1.1
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(AppException appException) {
        ResponseCode responseCode = appException.getResponseCode();
        return buildErrorResponse(responseCode);
    }

    /**
     * Handle type mismatch.
     * <p>
     * This function Handles type mismatch.
     * Specifically, it handles situations where the parameters passed in do not match the required data types.
     * code, message and timestamp if an exception occurs.
     * @param methodArTypeMismkEx
     * @return buildErrorResponse(responseCode)
     * @return "Invalid param '" + parameterName + "'!", responseCode.getHttpStatus()
     * @author HoangVu
     * @since 1.0
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException methodArTypeMismkEx) {
        String parameterName = methodArTypeMismkEx.getName();
        ResponseCode responseCode = (ResponseCode.INVALID_PARAM);
        if ("id".equals(parameterName)) {
            return buildErrorResponse(responseCode);
        }
        return new ResponseEntity<>("Invalid param '" + parameterName + "'!", responseCode.getHttpStatus());
    }

}

