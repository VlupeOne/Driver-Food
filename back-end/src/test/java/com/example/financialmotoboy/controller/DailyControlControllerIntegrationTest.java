package com.example.financialmotoboy.controller;

import static com.example.financialmotoboy.common.DailyControlConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.financialmotoboy.dto.DailyControlResponseDto;
import com.example.financialmotoboy.dto.DashboardSummaryDto;
import com.example.financialmotoboy.entity.DailyControl;
import com.example.financialmotoboy.repository.DailyControlRepository;
import com.example.financialmotoboy.service.DailyControlService;

@SpringBootTest
@Transactional
class DailyControlControllerIntegrationTest {

    @Autowired
    private DailyControlService dailyControlService;

    @Autowired
    private DailyControlRepository dailyControlRepository;

    @BeforeEach
    void setup() {
        dailyControlRepository.deleteAll();
    }

    // =========================
    // CREATE
    // =========================

    @Test
    void shouldCreateDailyControlAndPersistInDatabase() {

        DailyControlResponseDto response =
                dailyControlService.create(validRequest());

        assertThat(response.getId()).isNotNull();
        assertThat(response.getProfit()).isEqualByComparingTo("90.00");

        List<DailyControl> saved = dailyControlRepository.findAll();
        assertThat(saved).hasSize(1);
    }

    @Test
    void shouldCreateUsingZeroWhenGasolineAndFoodAreNull() {

        DailyControlResponseDto response =
                dailyControlService.create(requestWithoutExpenses());

        assertThat(response.getGasolina()).isEqualByComparingTo("0");
        assertThat(response.getComida()).isEqualByComparingTo("0");
    }

    @Test
    void shouldCreateWithExtrasAndPersistCorrectly() {

        DailyControlResponseDto response =
                dailyControlService.create(requestWithExtras());

        assertThat(response.getExtras()).hasSize(1);
        assertThat(response.getExtras().get(0).getDescription()).isEqualTo("Pedágio");

        List<DailyControl> saved = dailyControlRepository.findAll();
        assertThat(saved.get(0).getExtras()).hasSize(1);
    }

    // =========================
    // FIND ALL
    // =========================

    @Test
    void shouldReturnAllControls() {

        dailyControlRepository.save(entityBasic());

        List<DailyControlResponseDto> result =
                dailyControlService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFaturamento()).isEqualByComparingTo("100.00");
    }

    // =========================
    // FIND RECENT
    // =========================

    @Test
    void shouldReturnRecentControls() {

        dailyControlRepository.save(entityBasic());

        List<DailyControlResponseDto> result =
                dailyControlService.findRecent(5);

        assertThat(result).hasSize(1);
    }

    // =========================
    // SUMMARY
    // =========================

    @Test
    void shouldCalculateSummaryWithRealDatabaseData() {

        DailyControl c1 = entityForSummary1();
        c1.setRecordedAt(LocalDateTime.now()); // ✔ FIX só aqui

        DailyControl c2 = entityForSummary2();
        c2.setRecordedAt(LocalDateTime.now()); // ✔ FIX só aqui

        dailyControlRepository.save(c1);
        dailyControlRepository.save(c2);

        DashboardSummaryDto summary =
                dailyControlService.getSummary();

        assertThat(summary.getRevenue()).isEqualByComparingTo("350.00");
        assertThat(summary.getExpenses()).isEqualByComparingTo("65.00");
        assertThat(summary.getProfit()).isEqualByComparingTo("285.00");
        assertThat(summary.getDeliveriesCount()).isEqualTo(2);
   }

    @Test
    void shouldReturnEmptySummaryWhenNoDataExists() {

        DashboardSummaryDto summary =
                dailyControlService.getSummary();

        assertThat(summary.getRevenue()).isEqualByComparingTo("0");
        assertThat(summary.getExpenses()).isEqualByComparingTo("0");
        assertThat(summary.getProfit()).isEqualByComparingTo("0");
        assertThat(summary.getDeliveriesCount()).isZero();
    }
}