export interface ExtraExpense {
  description: string;
  amount: number;
}

export interface DailyControl {
  id: number;
  faturamento: number;
  gasolina: number;
  comida: number;
  observation?: string;
  date: string;
  recordedAt: string;
  profit: number;
  extras: ExtraExpense[];
}
