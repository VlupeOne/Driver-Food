export type TransactionType = 'receita' | 'despesa';

export interface Transaction {
  id: string;
  type: TransactionType;
  categoryId: string;
  amount: number;
  description: string;
  date: string;
  note?: string;
}
