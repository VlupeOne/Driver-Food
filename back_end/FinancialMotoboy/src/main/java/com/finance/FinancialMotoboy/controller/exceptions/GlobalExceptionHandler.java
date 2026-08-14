package com.finance.FinancialMotoboy.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.FinancialMotoboy.controller.dtos.ErrorResponse;
import com.finance.FinancialMotoboy.service.exceptions.AttributeAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


        @ExceptionHandler(AttributeAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleAttributeAlreadyExists(
                AttributeAlreadyExistsException ex,
                HttpServletRequest request) {

                return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        409,
                        "CONFLICT",
                        ex.getMessage(),
                        request.getRequestURI()
                ));
        }

        @ExceptionHandler(AuthorizationDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAuthorizationDenied(
                AuthorizationDeniedException ex, HttpServletRequest request) {

                return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        403,
                        "FORBIDDEN",
                        "You do not have permission to access this resource.",
                        request.getRequestURI()
                ));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDenied(
                AccessDeniedException ex, HttpServletRequest request) {

                return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        403,
                        "FORBIDDEN",
                        "Acesso negado",
                        request.getRequestURI()
                ));
        }


        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
                return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        500,
                        "INTERNAL_SERVER_ERROR",
                        "Ocorreu um erro interno",
                        request.getRequestURI()
                ));
        }
}
