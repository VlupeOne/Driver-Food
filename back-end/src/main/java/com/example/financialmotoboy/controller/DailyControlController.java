package com.example.financialmotoboy.controller;

import com.example.financialmotoboy.dto.DashboardSummaryDto;
import com.example.financialmotoboy.dto.DailyControlRequestDto;
import com.example.financialmotoboy.dto.DailyControlResponseDto;
import com.example.financialmotoboy.service.DailyControlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class DailyControlController {

    private final DailyControlService dailyControlService;

    public DailyControlController(DailyControlService dailyControlService) {
        this.dailyControlService = dailyControlService;
    }

    @GetMapping
    public ResponseEntity<List<DailyControlResponseDto>> getAll() {
        return ResponseEntity.ok(dailyControlService.findAll());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<DailyControlResponseDto>> getRecent(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dailyControlService.findRecent(limit));
    }

    @PostMapping
    public ResponseEntity<DailyControlResponseDto> create(@Valid @RequestBody DailyControlRequestDto request) {
        DailyControlResponseDto created = dailyControlService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<DashboardSummaryDto> getSummary() {
        return ResponseEntity.ok(dailyControlService.getSummary());
    }
}
