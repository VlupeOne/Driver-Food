package com.finance.FinancialMotoboy.service;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

    public void addHttpOnlyCookie(String name, String value, int maxAge, HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie
                .from(name, value)
                .httpOnly(true)
                .secure(false) // desenvolvimento
                .path("/")
                .maxAge(Duration.ofSeconds(maxAge))
                .sameSite("Lax")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }
}
