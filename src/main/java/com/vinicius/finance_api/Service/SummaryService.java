package com.vinicius.finance_api.Service;

    import com.vinicius.finance_api.Entities.MonthlySummary;
    import com.vinicius.finance_api.Entities.Transaction;
    import com.vinicius.finance_api.Enums.TransactionType;
    import com.vinicius.finance_api.Repositories.MonthlySumaryRepository;
import com.vinicius.finance_api.Repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
    import java.util.List;

@Service
public class SummaryService {
    private final TransactionRepository transactionRepository;
    private final MonthlySumaryRepository monthlySumaryRepository;

    public SummaryService(MonthlySumaryRepository monthlySummaryRepository, TransactionRepository transactionRepository) {
        this.monthlySumaryRepository = monthlySummaryRepository;
        this.transactionRepository = transactionRepository;
    }

        public List<Transaction> getTransactionsPerDate(LocalDate start, LocalDate finish) {
            return transactionRepository.findByDateBetween(start, finish);
        }

        public Double totalIncome(List<Transaction> transactions) {
            return transactions.stream()
                    .filter(t -> t.getType() == TransactionType.INCOME)
                    .mapToDouble(Transaction::getValue)
                    .sum();
        }

        public Double totalExpense(List<Transaction> transactions) {
            return transactions.stream()
                    .filter(t -> t.getType() == TransactionType.EXPENSE)
                    .mapToDouble(Transaction::getValue)
                    .sum();
        }

        public Double balance(Double totalIncome, Double totalExpensive) {
            return totalIncome - totalExpensive;
        }

        public Integer totalTransactions(List<Transaction> transactions) {
            return transactions.size();
        }

    public MonthlySummary gerarSummary(LocalDate start, LocalDate end) {
        List<Transaction> transactions = getTransactionsPerDate(start, end);

        Double income = totalIncome(transactions);
        Double expense = totalExpense(transactions);
        Double bal = balance(income, expense);
        Integer count = totalTransactions(transactions);

        MonthlySummary summary = new MonthlySummary();
        summary.setTotalIncome(income);
        summary.setTotalExpense(expense);
        summary.setBalance(bal);
        summary.setTotalTransactions(count);
        summary.setInitialDate(start);
        summary.setFinalDate(end);

        return monthlySumaryRepository.save(summary);
    }

    public  List<MonthlySummary> getAllSummaries() {
        return monthlySumaryRepository.findAll();
    }



}
