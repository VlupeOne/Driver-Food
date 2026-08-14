package com.example.financialmotoboy.controller;

import org.springframework.http.HttpHeaders;
import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.financialmotoboy.dto.AuthenticationResponse;
import com.example.financialmotoboy.dto.LoginRequest;
import com.example.financialmotoboy.dto.RegisterRequest;
import com.example.financialmotoboy.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authService.register(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", response.token())
                .httpOnly(true)
                .secure(false) // true em produção
                .path("/")
                .sameSite("strict")
                .maxAge(24 * 60 * 60) // 1 day
                .build();
        
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

@PostMapping("/login")
public ResponseEntity<Void> login(@RequestBody LoginRequest request) {

    AuthenticationResponse response = authService.login(request);

    ResponseCookie cookie = ResponseCookie.from("jwt", response.token())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("Strict")
            .maxAge(Duration.ofDays(1))
            .build();

    return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
}
}