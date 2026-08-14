package com.finance.FinancialMotoboy.config.exceptions;

import java.time.LocalDateTime;

import java.io.IOException;

import jakarta.servlet.ServletException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.http.MediaType;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.FinancialMotoboy.controller.dtos.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(
                response.getWriter(),
                new ErrorResponse(
                        LocalDateTime.now(),
                        401,
                        "UNAUTHORIZED",
                        "Autenticação necessária",
                        request.getRequestURI()
                )
        );
    }
}
