import { describe, expect, it } from 'vitest';
import { firstValueFrom, throwError } from 'rxjs';
import { DashboardService } from './dashboard.service';

describe('DashboardService', () => {
  it('should return fallback summary when backend data is unavailable', async () => {
    const dailyControlService = {
      getAll: () => throwError(() => new Error('backend unavailable')),
      getRecent: () => throwError(() => new Error('backend unavailable'))
    } as any;

    const service = new DashboardService(dailyControlService);

    const summary = await firstValueFrom(service.getDashboardSummary());

    expect(summary.revenue).toBeGreaterThan(0);
    expect(summary.recentTransactions.length).toBeGreaterThan(0);
  });
});
