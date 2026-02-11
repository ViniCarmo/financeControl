package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.dto.SummaryResponseDto;
import com.vinicius.finance_api.entities.Summary;
import com.vinicius.finance_api.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SummaryResponseDto> gerarSummaryManual(@PathVariable Integer userId) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.plusMonths(1).withDayOfMonth(1).minusDays(1);

        Summary summary = summaryService.gerarSummary(userId, start, end);

        SummaryResponseDto dto = new SummaryResponseDto(
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Summary>> getBySummaryUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(summaryService.getSummariesByUserId(userId));
    }


    @GetMapping
    public ResponseEntity<List<Summary>> getAll() {
      return ResponseEntity.ok(summaryService.getAllSummaries());
}
}
