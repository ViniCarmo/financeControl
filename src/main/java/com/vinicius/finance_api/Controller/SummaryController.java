package com.vinicius.finance_api.Controller;

import com.vinicius.finance_api.Dto.MonthlySummaryResponseDto;
import com.vinicius.finance_api.Entities.MonthlySummary;
import com.vinicius.finance_api.Service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping("/generate")
    public ResponseEntity<MonthlySummaryResponseDto> gerarSummaryManual() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.plusMonths(1).withDayOfMonth(1).minusDays(1);

        MonthlySummary summary = summaryService.gerarSummary(start, end);

        MonthlySummaryResponseDto dto = new MonthlySummaryResponseDto(
                summary.getId(),
                summary.getTotalIncome(),
                summary.getTotalExpense(),
                summary.getBalance(),
                summary.getTotalTransactions(),
                summary.getInitialDate(),
                summary.getFinalDate()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<MonthlySummary>> getAll() {
      return ResponseEntity.ok(summaryService.getAllSummaries());
}
}
