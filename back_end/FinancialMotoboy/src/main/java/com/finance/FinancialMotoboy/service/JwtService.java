package com.finance.FinancialMotoboy.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private int expiration;

        public String generateToken(Authentication authentication) {

                UserDetails mainUser = (UserDetails) authentication.getPrincipal();

                SecretKey key = Keys.hmacShaKeyFor(
                        secret.getBytes(StandardCharsets.UTF_8)
                );

                Date now = new Date();

                return Jwts.builder()
                        .subject(mainUser.getUsername())
                        .issuedAt(now)
                        .expiration(
                                new Date(now.getTime() + expiration * 1000L)
                        )
                        .signWith(key)
                        .compact();
        }

        public String extractUsername(String token) {
                SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                return Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject();
        }


        public boolean isTokenValid(String token) {

                try {
                        extractUsername(token);
                        return true;

                } catch (JwtException | IllegalArgumentException e) {
                        return false;
                }
        }

}