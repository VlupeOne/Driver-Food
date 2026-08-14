package com.finance.FinancialMotoboy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.FinancialMotoboy.controller.dtos.AddressResponse;
import com.finance.FinancialMotoboy.service.MotoboyService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/motoboy")
@PreAuthorize("hasRole('MOTOBOY')")
public class MotoboyController {
    private final MotoboyService service;

    public MotoboyController(MotoboyService service) {
        this.service = service;
    }

    @PostMapping("saveAdress")
    public ResponseEntity<Void> saveAdress(Authentication authentication, @RequestBody AddressResponse addressResponse) {
        service.saveAddress(authentication, addressResponse);
        
        return ResponseEntity.ok().build();
    }

    @PutMapping("updateAddress")
    public ResponseEntity<Void> updateAddress(Authentication authentication, @RequestBody AddressResponse addressResponse) {
        service.updateAddress(authentication, addressResponse);
        
        return ResponseEntity.ok().build();
    }
    
}
