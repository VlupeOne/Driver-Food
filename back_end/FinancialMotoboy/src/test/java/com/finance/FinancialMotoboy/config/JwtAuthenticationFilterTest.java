package com.finance.FinancialMotoboy.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.entities.User;
import com.finance.FinancialMotoboy.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

        private static class FakeUser extends User {

                @Override
                public DefaultUserResponse toResponse() {
                        return null;
                }
        }

        @Mock
        private JwtService jwtService;

        @Mock
        private UserDetailsService userDetailsService;

        @Mock
        private FilterChain filterChain;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        private JwtAuthenticationFilter filter;

        @BeforeEach
        void setup() {
                filter = new JwtAuthenticationFilter(
                        jwtService,
                        userDetailsService
                );
                SecurityContextHolder.clearContext();
        }

        @Test
        void shouldAuthenticateUserWhenJwtIsValid() throws Exception {

                Cookie cookie = new Cookie("jwt", "token-valido");

                when(request.getCookies())
                        .thenReturn(new Cookie[]{cookie});
                when(jwtService.isTokenValid("token-valido"))
                        .thenReturn(true);
                when(jwtService.extractUsername("token-valido"))
                        .thenReturn("user@email.com");

                User user = new FakeUser();

                user.setEmail("user@email.com");
                user.setPassword("password");

                when(userDetailsService.loadUserByUsername("user@email.com"))
                        .thenReturn(user);

                filter.doFilterInternal(
                        request,
                        response,
                        filterChain
                );

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                assertNotNull(authentication);
                assertEquals(
                        "user@email.com",
                        authentication.getName()
                );

                assertInstanceOf(
                        User.class,
                        authentication.getPrincipal()
                );

                verify(filterChain)
                        .doFilter(request, response);
        }

        @Test
        void shouldReturn401WhenUserDoesNotExist() throws Exception {
                Cookie cookie = new Cookie("jwt", "token-valido");

                when(request.getCookies())
                        .thenReturn(new Cookie[]{cookie});
                when(jwtService.isTokenValid("token-valido"))
                        .thenReturn(true);
                when(jwtService.extractUsername("token-valido"))
                        .thenReturn("user@email.com");
                when(userDetailsService.loadUserByUsername("user@email.com"))
                        .thenThrow(
                                new UsernameNotFoundException(
                                "Usuário não encontrado"
                                )
                        );

                StringWriter writer = new StringWriter();

                PrintWriter printWriter = new PrintWriter(writer);

                when(response.getWriter())
                        .thenReturn(printWriter);

                filter.doFilterInternal(
                        request,
                        response,
                        filterChain
                );

                verify(response)
                        .setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                );

                verify(filterChain, never())
                        .doFilter(request, response);

                printWriter.flush();

                assertTrue(
                        writer.toString()
                                .contains("Credenciais inválidas")
                );
        }

        @Test
        void shouldContinueChainWhenJwtIsInvalid() throws Exception {

                Cookie cookie = new Cookie("jwt", "token-invalido");

                when(request.getCookies())
                        .thenReturn(new Cookie[]{cookie});

                when(jwtService.isTokenValid("token-invalido"))
                        .thenReturn(false);

                filter.doFilterInternal(
                        request,
                        response,
                        filterChain
                );

                verify(filterChain)
                        .doFilter(request, response);

                assertNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                );
        }

        @Test
        void shouldIgnoreAuthRoutes() {

                when(request.getServletPath())
                        .thenReturn("/auth/login");

                assertTrue(
                filter.shouldNotFilter(request)
                );
        }
        }
