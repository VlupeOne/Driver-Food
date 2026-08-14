package com.finance.FinancialMotoboy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.FinancialMotoboy.controller.dtos.IfoodAuthorizationCodeRequest;
import com.finance.FinancialMotoboy.controller.dtos.IfoodRefreshTokenRequest;
import com.finance.FinancialMotoboy.controller.dtos.IfoodTokenResponse;
import com.finance.FinancialMotoboy.controller.dtos.IfoodUserCodeResponse;
import com.finance.FinancialMotoboy.service.IfoodAuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ifood/auth")
public class IfoodAuthController {

    private final IfoodAuthService ifoodAuthService;

    public IfoodAuthController(IfoodAuthService ifoodAuthService) {
        this.ifoodAuthService = ifoodAuthService;
    }

    @PostMapping("/user-code")
    public ResponseEntity<IfoodUserCodeResponse> requestUserCode() {
        return ResponseEntity.ok(ifoodAuthService.requestUserCode());
    }

    @PostMapping("/token")
    public ResponseEntity<IfoodTokenResponse> requestToken(
            @Valid @RequestBody IfoodAuthorizationCodeRequest request) {
        return ResponseEntity.ok(ifoodAuthService.requestAccessToken(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<IfoodTokenResponse> refreshToken(
            @Valid @RequestBody IfoodRefreshTokenRequest request) {
        return ResponseEntity.ok(ifoodAuthService.refreshAccessToken(request));
    }
}
