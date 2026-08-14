package com.finance.FinancialMotoboy.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.FinancialMotoboy.controller.dtos.DailyControlsRequest;
import com.finance.FinancialMotoboy.controller.dtos.DailyControlsResponse;
import com.finance.FinancialMotoboy.service.DailyControlsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/dailyControls")
@PreAuthorize("hasRole('MOTOBOY')")
public class DailyControlsController {

    private final DailyControlsService dailyControlsService;

    public DailyControlsController(DailyControlsService dailyControlsService) {
        this.dailyControlsService = dailyControlsService;
    }

    @GetMapping("getAll")
    public ResponseEntity<List<DailyControlsResponse>> getAll(Authentication authentication) {
        return ResponseEntity.ok(dailyControlsService.findAll(authentication));
    }

    @PostMapping("create")
    public ResponseEntity<DailyControlsResponse> create(Authentication authentication, @Valid @RequestBody DailyControlsRequest response) {
        DailyControlsResponse created = dailyControlsService.create(authentication, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
