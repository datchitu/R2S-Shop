package com.r2s.R2Sshop.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class BaseRestController {
    public ResponseEntity<Map<String, Object>> success(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "OK");
        response.put("data", data);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> error(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", null);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}

