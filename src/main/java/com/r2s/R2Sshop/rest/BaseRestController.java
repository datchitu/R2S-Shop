package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.constants.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BaseRestController{
    private final Timestamp ts = Timestamp.from(ZonedDateTime.now().toInstant());
    /**
     * Return data and HttpStatus.OK.
     * <p>
     * This function reports the status HttpStatus.OK and returns data as a JSON file
     * if the function retrieves the data successfully.
     * @param data
     * @return ResponseEntity<Map<String, Object>>(response, HttpStatus.OK)
     * @author HoangVu
     * @since 1.1
     */
    public ResponseEntity<Map<String, Object>> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "OK");
        response.put("data", data);
        response.put("timestamp",ts);
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
     * @since 1.2
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(ResponseCode responseCode, Object error) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(responseCode.getCode())
                .message(responseCode.getMessage())
                .error(error)
                .timestamp(ts)
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
     * @since 1.2
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(AppException appException) {
        ResponseCode responseCode = appException.getResponseCode();
        return buildErrorResponse(responseCode, null);
    }
    /**
     * Handle type mismatch.
     * <p>
     * This function handles type mismatch.
     * Specifically, it handles situations where the parameters passed in do not match the required data types.
     * code, message and timestamp if an exception occurs.
     * @param methodArTypeMismchEx
     * @return buildErrorResponse(responseCode)
     * @author HoangVu
     * @since 1.2
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException methodArTypeMismchEx) {
        String parameterName = methodArTypeMismchEx.getName();
        ResponseCode responseCode = (ResponseCode.INVALID_PARAM);
        return buildErrorResponse(responseCode, null);
    }
    /**
     * Configure BadCredentialsException with Builder.
     * <p>
     * This function configures the BadCredentialsException with Builder and then reports the status HttpStatus error,
     * returns the information including.
     * code, message and timestamp if an exception occurs.
     * @param badCredentialsEx
     * @return buildErrorResponse(ResponseCode.FAILURE_LOGIN)
     * @author HoangVu
     * @since 1.1
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException badCredentialsEx) {
        return buildErrorResponse(ResponseCode.FAILURE_SIGNIN, null);
    }
    /**
     * Configure AuthenticationException with Builder.
     * <p>
     * This function configures the AuthenticationException with Builder and then reports the status HttpStatus error,
     * returns the information including.
     * code, message and timestamp if an exception occurs.
     * @param authenticationEx
     * @return buildErrorResponse(ResponseCode.FAILURE_LOGIN)
     * @author HoangVu
     * @since 1.1
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handelAuthenticationException(AuthenticationException authenticationEx) {
        return buildErrorResponse(ResponseCode.AUTHENTICATION_ERROR, null);
    }
    /**
     * Configure MissingServletRequestParameterException with Builder.
     * <p>
     * This function configures the MissingServletRequestParameterException with Builder
     * and then reports the status HttpStatus error,
     * returns the information including.
     * code, message and timestamp if an exception occurs.
     * @param missingParamEx
     * @return buildErrorResponse(ResponseCode.MISSING_PARAM)
     * @author HoangVu
     * @since 1.1
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException missingParamEx) {
        return buildErrorResponse(ResponseCode.MISSING_PARAM, null);
    }
    /**
     * Configure MethodArgumentNotValidException with Builder.
     * <p>
     * This function configures the MethodArgumentNotValidException with Builder
     * and then reports the status HttpStatus error,
     * returns the message.
     * @param mthdAgmtNtVldEx
     * @return message if any information is missing
     * @author HoangVu
     * @since 1.1
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException mthdAgmtNtVldEx) {
        Map<String, Object> fieldErrors = new HashMap<>();
        mthdAgmtNtVldEx.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));
        return buildErrorResponse(ResponseCode.INVALID_VALUE, fieldErrors);
    }
}

