package com.finance.FinancialMotoboy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.finance.FinancialMotoboy.controller.dtos.IfoodAuthorizationCodeRequest;
import com.finance.FinancialMotoboy.controller.dtos.IfoodRefreshTokenRequest;
import com.finance.FinancialMotoboy.controller.dtos.IfoodTokenResponse;
import com.finance.FinancialMotoboy.controller.dtos.IfoodUserCodeResponse;
import com.finance.FinancialMotoboy.service.exceptions.IfoodAuthenticationException;

@Service
public class IfoodAuthService {

    private static final String AUTHORIZATION_CODE_GRANT = "authorization_code";
    private static final String REFRESH_TOKEN_GRANT = "refresh_token";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String clientId;
    private final String clientSecret;

    public IfoodAuthService(
            @Value("${ifood.auth.base-url:https://merchant-api.ifood.com.br/authentication/v1.0}") String baseUrl,
            @Value("${ifood.auth.client-id:}") String clientId,
            @Value("${ifood.auth.client-secret:}") String clientSecret) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = removeTrailingSlash(baseUrl);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public IfoodUserCodeResponse requestUserCode() {
        validateClientId();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("clientId", clientId);

        return postForm("/oauth/userCode", form, IfoodUserCodeResponse.class);
    }

    public IfoodTokenResponse requestAccessToken(IfoodAuthorizationCodeRequest request) {
        validateClientCredentials();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grantType", AUTHORIZATION_CODE_GRANT);
        form.add("clientId", clientId);
        form.add("clientSecret", clientSecret);
        form.add("authorizationCode", request.authorizationCode());
        form.add("authorizationCodeVerifier", request.authorizationCodeVerifier());

        return postForm("/oauth/token", form, IfoodTokenResponse.class);
    }

    public IfoodTokenResponse refreshAccessToken(IfoodRefreshTokenRequest request) {
        validateClientCredentials();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grantType", REFRESH_TOKEN_GRANT);
        form.add("clientId", clientId);
        form.add("clientSecret", clientSecret);
        form.add("refreshToken", request.refreshToken());

        return postForm("/oauth/token", form, IfoodTokenResponse.class);
    }

    private <T> T postForm(String path, MultiValueMap<String, String> form, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(form, headers);

        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + path,
                    HttpMethod.POST,
                    entity,
                    responseType
            );

            T body = response.getBody();
            if (body == null) {
                throw new IfoodAuthenticationException(
                        HttpStatus.BAD_GATEWAY,
                        "A API do iFood respondeu sem corpo."
                );
            }

            return body;
        } catch (HttpStatusCodeException ex) {
            throw new IfoodAuthenticationException(
                    ex.getStatusCode(),
                    buildIfoodErrorMessage(ex)
            );
        } catch (ResourceAccessException ex) {
            throw new IfoodAuthenticationException(
                    HttpStatus.BAD_GATEWAY,
                    "Não foi possível conectar à API de autenticação do iFood."
            );
        }
    }

    private void validateClientCredentials() {
        validateClientId();

        if (clientSecret == null || clientSecret.isBlank()) {
            throw new IfoodAuthenticationException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Configure a variável IFOOD_CLIENT_SECRET antes de solicitar tokens do iFood."
            );
        }
    }

    private void validateClientId() {
        if (clientId == null || clientId.isBlank()) {
            throw new IfoodAuthenticationException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Configure a variável IFOOD_CLIENT_ID antes de autenticar com o iFood."
            );
        }
    }

    private String buildIfoodErrorMessage(HttpStatusCodeException ex) {
        String body = ex.getResponseBodyAsString();
        if (body == null || body.isBlank()) {
            return "Falha na autenticação com o iFood: " + ex.getStatusText();
        }

        return "Falha na autenticação com o iFood: " + body;
    }

    private String removeTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "https://merchant-api.ifood.com.br/authentication/v1.0";
        }

        return value.endsWith("/")
                ? value.substring(0, value.length() - 1)
                : value;
    }
}
