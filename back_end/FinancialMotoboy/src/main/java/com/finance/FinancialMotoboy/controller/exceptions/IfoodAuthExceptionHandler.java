package com.finance.FinancialMotoboy.controller.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.FinancialMotoboy.controller.IfoodAuthController;
import com.finance.FinancialMotoboy.controller.dtos.ErrorResponse;
import com.finance.FinancialMotoboy.service.exceptions.IfoodAuthenticationException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice(assignableTypes = IfoodAuthController.class)
public class IfoodAuthExceptionHandler {

    @ExceptionHandler(IfoodAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleIfoodAuthentication(
            IfoodAuthenticationException ex,
            HttpServletRequest request) {

        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        String error = status != null ? status.name() : "IFOOD_AUTHENTICATION_ERROR";

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        ex.getStatusCode().value(),
                        error,
                        ex.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        400,
                        "BAD_REQUEST",
                        message,
                        request.getRequestURI()
                ));
    }
}
