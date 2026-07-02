import { Transaction } from './transaction.model';

export interface ChartPoint {
  label: string;
  value: number;
}

export interface DashboardSummary {
  revenue: number;
  profit: number;
  expenses: number;
  averageTicket: number;
  deliveriesCount: number;
  recentTransactions: Transaction[];
  revenueSeries: ChartPoint[];
  profitSeries: ChartPoint[];
  categoryBreakdown: ChartPoint[];
}
