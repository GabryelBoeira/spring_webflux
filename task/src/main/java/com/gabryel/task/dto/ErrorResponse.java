package com.gabryel.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(description = "Objeto contendo os dados de uma resposta de erro na API.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String message;
    private String error;
    private String path;
    private LocalDateTime timestamp;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters e Setters existentes
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

    // Novos getters e setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

        public Builder error(String error) {
            errorResponse.setError(error);
            return this;
        }

        public Builder path(String path) {
            errorResponse.setPath(path);
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            errorResponse.setTimestamp(timestamp);
            return this;
        }

        public ErrorResponse build() {
            return errorResponse;
        }
    }

    // Métodos factory melhorados
    public static ErrorResponse internalError(Exception e) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    public static ErrorResponse badRequest(Exception e) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    public static ErrorResponse notFound(Exception e) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }

    public static ErrorResponse fromStatus(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
    }
}
