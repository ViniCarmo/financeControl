package com.vinicius.finance_api.Service;

    import com.vinicius.finance_api.Entities.Summary;
    import com.vinicius.finance_api.Entities.Transaction;
    import com.vinicius.finance_api.Entities.User;
    import com.vinicius.finance_api.Enums.TransactionType;
    import com.vinicius.finance_api.Repositories.SummaryRepository;
import com.vinicius.finance_api.Repositories.TransactionRepository;
    import com.vinicius.finance_api.Repositories.UserRepository;
    import org.springframework.stereotype.Service;

import java.time.LocalDate;
    import java.util.List;

@Service
public class SummaryService {
    private final TransactionRepository transactionRepository;
    private final SummaryRepository monthlySumaryRepository;
    private final UserRepository userRepository;

    public SummaryService(SummaryRepository monthlySummaryRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.monthlySumaryRepository = monthlySummaryRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

        public List<Transaction> getTransactionsPerDate(Integer userId, LocalDate start, LocalDate finish) {
            return transactionRepository.findByUserIdAndDateBetween(userId, start, finish);
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

    public Summary gerarSummary(Integer userId, LocalDate start, LocalDate end) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = getTransactionsPerDate(userId, start, end);

        Double income = totalIncome(transactions);
        Double expense = totalExpense(transactions);
        Double bal = balance(income, expense);
        Integer count = totalTransactions(transactions);

        Summary summary = new Summary();
        summary.setTotalIncome(income);
        summary.setTotalExpense(expense);
        summary.setBalance(bal);
        summary.setTotalTransactions(count);
        summary.setInitialDate(start);
        summary.setFinalDate(end);
        summary.setUser(user);

        return monthlySumaryRepository.save(summary);
    }

    public  List<Summary> getAllSummaries() {
        return monthlySumaryRepository.findAll();
    }

    public List<Summary> getSummariesByUserId(Integer userId){
        return monthlySumaryRepository.findByUserId(userId);
    }



}
