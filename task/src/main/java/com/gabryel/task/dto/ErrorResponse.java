package com.gabryel.task.dto;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ErrorResponse errorResponse;

        private Builder() {
            errorResponse = new ErrorResponse();
        }

        public Builder status(int status) {
            errorResponse.setStatus(status);
            return this;
        }

        public Builder message(String message) {
            errorResponse.setMessage(message);
            return this;
        }

        public ErrorResponse build() {
            return errorResponse;
        }
    }

    public static ErrorResponse internalError(RuntimeException e) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
    }

}
