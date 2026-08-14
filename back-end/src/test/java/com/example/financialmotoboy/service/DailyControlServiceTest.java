package com.example.financialmotoboy.service;

import static com.example.financialmotoboy.common.DailyControlConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.example.financialmotoboy.dto.DailyControlRequestDto;
import com.example.financialmotoboy.dto.DailyControlResponseDto;
import com.example.financialmotoboy.dto.DashboardSummaryDto;
import com.example.financialmotoboy.entity.DailyControl;
import com.example.financialmotoboy.repository.DailyControlRepository;

class DailyControlServiceTest {

    @Mock
    private DailyControlRepository dailyControlRepository;

    @InjectMocks
    private DailyControlService dailyControlService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateDailyControlWithCurrentDateWhenNotProvided() {

        when(dailyControlRepository.save(any()))
                .thenAnswer(i -> {
                    DailyControl saved = i.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        DailyControlResponseDto response =
                dailyControlService.create(validRequest());

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getDate()).isEqualTo(LocalDate.now());
        assertThat(response.getProfit()).isEqualByComparingTo("90.00");

        verify(dailyControlRepository).save(any());
    }

    @Test
    void shouldCreateDailyControlUsingZeroWhenGasolineAndFoodAreNull() {

        when(dailyControlRepository.save(any()))
                .thenAnswer(i -> {
                    DailyControl saved = i.getArgument(0);
                    saved.setId(2L);
                    return saved;
                });

        DailyControlResponseDto response =
                dailyControlService.create(requestWithoutExpenses());

        assertThat(response.getGasolina()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getComida()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getProfit()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldCreateDailyControlWithExtras() {

        when(dailyControlRepository.save(any()))
                .thenAnswer(i -> {
                    DailyControl saved = i.getArgument(0);
                    saved.setId(3L);
                    return saved;
                });

        DailyControlResponseDto response =
                dailyControlService.create(requestWithExtras());

        assertThat(response.getExtras()).hasSize(1);
        assertThat(response.getExtras().get(0).getDescription()).isEqualTo("Pedágio");
        assertThat(response.getProfit()).isEqualByComparingTo("155.00");
    }

    @Test
    void shouldFindAll() {

        when(dailyControlRepository.findAll())
                .thenReturn(List.of(entityBasic()));

        List<DailyControlResponseDto> result = dailyControlService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);

        verify(dailyControlRepository).findAll();
    }

    @Test
    void shouldFindRecent() {

        when(dailyControlRepository.findAllByOrderByDateDesc(any(Pageable.class)))
                .thenReturn(List.of(entityBasic()));

        List<DailyControlResponseDto> result =
                dailyControlService.findRecent(5);

        assertThat(result).hasSize(1);

        verify(dailyControlRepository)
                .findAllByOrderByDateDesc(PageRequest.of(0, 5));
    }

    @Test
    void shouldCalculateDashboardSummaryCorrectly() {

        when(dailyControlRepository.findAll())
                .thenReturn(List.of(entityForSummary1(), entityForSummary2()));

        DashboardSummaryDto summary = dailyControlService.getSummary();

        assertThat(summary.getRevenue()).isEqualByComparingTo("350.00");
        assertThat(summary.getExpenses()).isEqualByComparingTo("65.00");
        assertThat(summary.getProfit()).isEqualByComparingTo("285.00");
        assertThat(summary.getAverageTicket()).isEqualByComparingTo("175.00");
        assertThat(summary.getDeliveriesCount()).isEqualTo(2);
    }

    @Test
    void shouldReturnEmptyDashboardSummary() {

        when(dailyControlRepository.findAll()).thenReturn(List.of());

        DashboardSummaryDto summary = dailyControlService.getSummary();

        assertThat(summary.getRevenue()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getExpenses()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getProfit()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getAverageTicket()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(summary.getDeliveriesCount()).isZero();
    }

    @Test
    void shouldThrowBadRequestWhenRevenueIsNull() {
        assertThrows(BadRequestException.class,
                () -> dailyControlService.create(new DailyControlRequestDto()));
    }

    @Test
    void shouldThrowBadRequestWhenRevenueIsNegative() {
        assertThrows(BadRequestException.class,
                () -> dailyControlService.create(requestRevenueNegative()));
    }

    @Test
    void shouldThrowBadRequestWhenGasolineIsNegative() {
        assertThrows(BadRequestException.class,
                () -> dailyControlService.create(requestGasolineNegative()));
    }

    @Test
    void shouldThrowBadRequestWhenFoodIsNegative() {
        assertThrows(BadRequestException.class,
                () -> dailyControlService.create(requestFoodNegative()));
    }

    @Test
    void shouldThrowBadRequestWhenExtraIsNegative() {
        assertThrows(BadRequestException.class,
                () -> dailyControlService.create(requestExtraNegative()));
    }
}