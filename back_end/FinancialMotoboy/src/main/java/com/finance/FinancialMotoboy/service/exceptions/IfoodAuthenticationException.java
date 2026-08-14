package com.finance.FinancialMotoboy.service.exceptions;

import org.springframework.http.HttpStatusCode;

public class IfoodAuthenticationException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public IfoodAuthenticationException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
