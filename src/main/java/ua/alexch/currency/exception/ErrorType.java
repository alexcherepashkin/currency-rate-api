package ua.alexch.currency.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    APP_ERROR("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("Bad Request", HttpStatus.BAD_REQUEST),
    NOT_FOUND("Data Not Found", HttpStatus.NOT_FOUND),
    NOT_SUPPORTED("Method Not Allowed", HttpStatus.METHOD_NOT_ALLOWED);

    private final String error;
    private final HttpStatus status;

    ErrorType(String error, HttpStatus status) {
        this.error = error;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
