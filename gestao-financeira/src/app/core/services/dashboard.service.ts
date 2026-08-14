import { Injectable } from '@angular/core';
import { catchError, combineLatest, map, Observable, of } from 'rxjs';

import { DailyControlService } from './dailyControlService.service';

import { DashboardSummary, ChartPoint, DashboardTransaction } from '../models/dashboard.model';
import { DailyControl } from '../models/dailyControl.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(
    private dailyControlService: DailyControlService
  ) {}

  getDashboardSummary(): Observable<DashboardSummary> {
    return combineLatest([
      this.dailyControlService.getAll().pipe(
        catchError(() => of(this.getFallbackDailyControls()))
      ),
      this.dailyControlService.getRecent(6).pipe(
        catchError(() => of(this.getFallbackDailyControls().slice(0, 6)))
      )
    ]).pipe(
      map(([dailyControls, recentDailyControls]) => {

        const revenue = dailyControls.reduce(
          (acc, item) => acc + item.faturamento,
          0
        );

        const expenses = dailyControls.reduce(
          (acc, item) =>
            acc +
            item.gasolina +
            item.comida +
            item.extras.reduce((e, extra) => e + extra.amount, 0),
          0
        );

        const profit = dailyControls.reduce(
          (acc, item) => acc + item.profit,
          0
        );

        const deliveriesCount = dailyControls.length;

        const averageTicket =
          deliveriesCount > 0 ? revenue / deliveriesCount : 0;

        return {
          revenue,
          expenses,
          profit,
          averageTicket,
          deliveriesCount,
          recentTransactions: this.buildRecentTransactions(recentDailyControls),
          revenueSeries: this.buildRevenueSeries(dailyControls),
          profitSeries: this.buildProfitSeries(dailyControls),
          categoryBreakdown: this.buildCategoryBreakdown(dailyControls)
        };
      })
    );
  }

  private buildRecentTransactions(controls: DailyControl[]): DashboardTransaction[] {
    return controls.map((control) => ({
      description: control.observation?.trim() || 'Controle diário',
      amount: control.faturamento,
      type: control.profit >= 0 ? 'receita' : 'despesa',
      date: control.date
    }));
  }

  private buildRevenueSeries(
    controls: DailyControl[]
  ): ChartPoint[] {

    const interval = this.buildLastDays(6);

    return interval.map(label => ({
      label,
      value: controls
        .filter(c => this.formatDate(c.date) === label)
        .reduce((acc, c) => acc + c.faturamento, 0)
    }));
  }

  private buildProfitSeries(
    controls: DailyControl[]
  ): ChartPoint[] {

    const interval = this.buildLastDays(6);

    return interval.map(label => ({
      label,
      value: controls
        .filter(c => this.formatDate(c.date) === label)
        .reduce((acc, c) => acc + c.profit, 0)
    }));
  }

  private buildCategoryBreakdown(
    controls: DailyControl[]
  ): ChartPoint[] {

    const gasolina = controls.reduce(
      (acc, c) => acc + c.gasolina,
      0
    );

    const comida = controls.reduce(
      (acc, c) => acc + c.comida,
      0
    );

    const extras = controls.reduce(
      (acc, c) =>
        acc + c.extras.reduce((e, extra) => e + extra.amount, 0),
      0
    );

    const result: ChartPoint[] = [];

    if (gasolina > 0) {
      result.push({ label: 'Gasolina', value: gasolina });
    }

    if (comida > 0) {
      result.push({ label: 'Comida', value: comida });
    }

    if (extras > 0) {
      result.push({ label: 'Extras', value: extras });
    }

    return result.length
      ? result
      : [{ label: 'Sem despesas', value: 1 }];
  }

  private getFallbackDailyControls(): DailyControl[] {
    const today = new Date();

    return [
      {
        id: 1,
        faturamento: 780,
        gasolina: 120,
        comida: 80,
        observation: 'Venda do dia',
        date: new Date(today.getFullYear(), today.getMonth(), today.getDate() - 2).toISOString(),
        recordedAt: new Date(today.getFullYear(), today.getMonth(), today.getDate() - 2).toISOString(),
        profit: 580,
        extras: [{ description: 'Entrega expressa', amount: 40 }]
      },
      {
        id: 2,
        faturamento: 650,
        gasolina: 95,
        comida: 65,
        observation: 'Cliente recorrente',
        date: new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1).toISOString(),
        recordedAt: new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1).toISOString(),
        profit: 490,
        extras: [{ description: 'Imprevisto', amount: 25 }]
      },
      {
        id: 3,
        faturamento: 920,
        gasolina: 140,
        comida: 90,
        observation: 'Dia movimentado',
        date: today.toISOString(),
        recordedAt: today.toISOString(),
        profit: 690,
        extras: [{ description: 'Frete', amount: 35 }]
      }
    ];
  }

  private buildLastDays(days: number): string[] {
    const formatter = new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit'
    });

    return Array.from({ length: days }, (_, index) => {
      const date = new Date();
      date.setDate(date.getDate() - (days - 1 - index));
      return formatter.format(date);
    });
  }

  private formatDate(value: string): string {
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit'
    }).format(new Date(value));
  }
}
