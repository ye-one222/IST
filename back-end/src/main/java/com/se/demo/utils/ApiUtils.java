package com.se.demo.utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApiUtils {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        ApiResponse<T> response = new ApiResponse<>(true, data, null, LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String errorMessage, HttpStatus status) {
        ApiResponse<T> response = new ApiResponse<>(false, null, errorMessage, LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }

    public static class ApiResponse<T> {
        private boolean success;
        private T data;
        private String error;
        private LocalDateTime timestamp;

        public ApiResponse(boolean success, T data, String error, LocalDateTime timestamp) {
            this.success = success;
            this.data = data;
            this.error = error;
            this.timestamp = timestamp;
        }

        // Getters and setters
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}

