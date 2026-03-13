export type TransactionType = "INCOME" | "EXPENSE";

export interface User {
  id: number;
  username: string;
  email: string;
}

export interface Transaction {
  id: number;
  value: number;
  type: TransactionType;
  date: string;
  description: string;
}

export interface Summary {
  month: string;
  totalIncome: number;
  totalExpense: number;
  balance: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}
