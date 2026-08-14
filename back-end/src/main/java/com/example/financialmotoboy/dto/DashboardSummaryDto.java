package com.example.financialmotoboy.dto;

import java.math.BigDecimal;

public class DashboardSummaryDto {

    private BigDecimal revenue;
    private BigDecimal expenses;
    private BigDecimal profit;
    private BigDecimal averageTicket;
    private Integer deliveriesCount;

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getAverageTicket() {
        return averageTicket;
    }

    public void setAverageTicket(BigDecimal averageTicket) {
        this.averageTicket = averageTicket;
    }

    public Integer getDeliveriesCount() {
        return deliveriesCount;
    }

    public void setDeliveriesCount(Integer deliveriesCount) {
        this.deliveriesCount = deliveriesCount;
    }
}
