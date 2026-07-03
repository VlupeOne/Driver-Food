export interface ChartPoint {
  label: string;
  value: number;
}

export interface DashboardTransaction {
  description: string;
  amount: number;
  type: 'receita' | 'despesa';
  date: string;
}

export interface DashboardSummary {
  revenue: number;
  profit: number;
  expenses: number;
  averageTicket: number;
  deliveriesCount: number;
  recentTransactions: DashboardTransaction[];
  revenueSeries: ChartPoint[];
  profitSeries: ChartPoint[];
  categoryBreakdown: ChartPoint[];
}
