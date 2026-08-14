package com.example.financialmotoboy.controller;

import com.example.financialmotoboy.dto.DashboardSummaryDto;
import com.example.financialmotoboy.service.DailyControlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DailyControlService dailyControlService;

    public DashboardController(DailyControlService dailyControlService) {
        this.dailyControlService = dailyControlService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDto> getSummary() {
        return ResponseEntity.ok(dailyControlService.getSummary());
    }
}
