package com.finance.FinancialMotoboy.services;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.controller.dtos.MotoboyResponse;
import com.finance.FinancialMotoboy.entities.User;
import com.finance.FinancialMotoboy.repositories.UserRepository;
import com.finance.FinancialMotoboy.service.UserService;
import com.finance.FinancialMotoboy.service.exceptions.CpfAlreadyExistsException;
import com.finance.FinancialMotoboy.service.exceptions.EmailAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;


    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "teste@email.com";
        String cpf = "12345678900";

        when(repository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () ->
                service.verifyAttributes(email, cpf)
        );

        verify(repository).existsByEmail(email);
        verify(repository, never()).existsByCpf(any());
    }


    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        String email = "teste@email.com";
        String cpf = "12345678900";

        when(repository.existsByEmail(email))
                .thenReturn(false);

        when(repository.existsByCpf(cpf))
                .thenReturn(true);

        assertThrows(CpfAlreadyExistsException.class, () ->
                service.verifyAttributes(email, cpf)
        );

        verify(repository).existsByEmail(email);
        verify(repository).existsByCpf(cpf);
    }


    @Test
    void shouldNotThrowExceptionWhenEmailAndCpfAreAvailable() {
        String email = "teste@email.com";
        String cpf = "12345678900";

        when(repository.existsByEmail(email))
                .thenReturn(false);

        when(repository.existsByCpf(cpf))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                service.verifyAttributes(email, cpf)
        );

        verify(repository).existsByEmail(email);
        verify(repository).existsByCpf(cpf);
    }


    @Test
    void shouldReturnAuthenticatedUser() {
        Authentication authentication = mock(Authentication.class);
        User mockUser = mock(User.class);

        when(authentication.getName())
                .thenReturn("teste@email.com");

        when(repository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(mockUser));


        User result = service.getAuthenticated(authentication);


        assertEquals(mockUser, result);

        verify(authentication).getName();
        verify(repository)
                .findByEmail("teste@email.com");
    }


    @Test
    void shouldThrowExceptionWhenAuthenticatedUserDoesNotExist() {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("teste@email.com");

        when(repository.findByEmail("teste@email.com"))
                .thenReturn(Optional.empty());


        assertThrows(UsernameNotFoundException.class, () ->
                service.getAuthenticated(authentication)
        );


        verify(authentication).getName();
        verify(repository)
                .findByEmail("teste@email.com");
    }


    @Test
    void shouldReturnAuthenticatedUserResponse() {
        Authentication authentication = mock(Authentication.class);
        User mockUser = mock(User.class);

        DefaultUserResponse response = new MotoboyResponse(
                "Teste",
                "teste@email.com",
                "Honda CG",
                "ABC1234",
                null,
                null
        );

        when(authentication.getName())
                .thenReturn("teste@email.com");

        when(repository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(mockUser));

        when(mockUser.toResponse())
                .thenReturn(response);


        DefaultUserResponse result =
                service.getAuthenticatedUser(authentication);


        assertEquals(response, result);

        verify(mockUser).toResponse();
    }

}
