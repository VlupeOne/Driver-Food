import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  ViewChild,
  PLATFORM_ID,
  Inject
} from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Chart, registerables } from 'chart.js';
import { take } from 'rxjs/operators';
import { DashboardService } from '../../core/services/dashboard.service';
import { SummaryCardComponent } from '../../shared/components/summary-card/summary-card.component';
import { DashboardSummary } from '../../core/models/dashboard.model';

Chart.register(...registerables);

@Component({
  standalone: true,
  selector: 'app-dashboard',
  imports: [CommonModule, RouterModule, SummaryCardComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements AfterViewInit, OnDestroy {
  @ViewChild('revenueCanvas') revenueCanvas?: ElementRef<HTMLCanvasElement>;
  @ViewChild('profitCanvas') profitCanvas?: ElementRef<HTMLCanvasElement>;
  @ViewChild('categoryCanvas') categoryCanvas?: ElementRef<HTMLCanvasElement>;

  summary$!: Observable<DashboardSummary>;
  summary?: DashboardSummary;
  private charts: Chart[] = [];

  constructor(
    private dashboardService: DashboardService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.summary$ = this.dashboardService.getDashboardSummary();
  }

  ngAfterViewInit(): void {
    this.summary$.pipe(take(1)).subscribe((summary) => {
      this.summary = summary;
      this.renderCharts(summary);
    });
  }

  ngOnDestroy(): void {
    this.charts.forEach((chart) => chart.destroy());
  }

  private renderCharts(summary: DashboardSummary): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    if (this.revenueCanvas) {
      this.charts.push(
        new Chart(this.revenueCanvas.nativeElement, {
          type: 'line',
          data: {
            labels: summary.revenueSeries.map((item) => item.label),
            datasets: [
              {
                label: 'Faturamento',
                data: summary.revenueSeries.map((item) => item.value),
                borderColor: '#4F46E5',
                backgroundColor: 'rgba(79, 70, 229, 0.16)',
                fill: true,
                tension: 0.35,
                pointRadius: 4
              }
            ]
          },
          options: {
            responsive: true,
            plugins: {
              legend: { display: false }
            },
            scales: {
              x: { grid: { display: false } },
              y: { beginAtZero: true }
            }
          }
        })
      );
    }

    if (this.profitCanvas) {
      this.charts.push(
        new Chart(this.profitCanvas.nativeElement, {
          type: 'bar',
          data: {
            labels: summary.profitSeries.map((item) => item.label),
            datasets: [
              {
                label: 'Lucro',
                data: summary.profitSeries.map((item) => item.value),
                backgroundColor: '#10B981'
              }
            ]
          },
          options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: {
              x: { grid: { display: false } },
              y: { beginAtZero: true }
            }
          }
        })
      );
    }

    if (this.categoryCanvas) {
      this.charts.push(
        new Chart(this.categoryCanvas.nativeElement, {
          type: 'doughnut',
          data: {
            labels: summary.categoryBreakdown.map((item) => item.label),
            datasets: [
              {
                data: summary.categoryBreakdown.map((item) => item.value),
                backgroundColor: ['#F97316', '#EF4444', '#10B981', '#8B5CF6', '#0EA5E9']
              }
            ]
          },
          options: {
            responsive: true,
            plugins: {
              legend: {
                position: 'bottom'
              }
            }
          }
        })
      );
    }
  }
}
