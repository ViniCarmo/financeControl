package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.dto.SummaryResponseDto;
import com.vinicius.finance_api.entities.Summary;
import com.vinicius.finance_api.entities.User;
import com.vinicius.finance_api.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/generate/")
    public ResponseEntity<SummaryResponseDto> gerarSummaryManual(@AuthenticationPrincipal User loggedUser) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.plusMonths(1).withDayOfMonth(1).minusDays(1);

        Summary summary = summaryService.gerarSummary(loggedUser.getId(), start, end);

        return ResponseEntity.ok(new SummaryResponseDto(
                summary.getId(),
                summary.getTotalIncome(),
                summary.getTotalExpense(),
                summary.getBalance(),
                summary.getTotalTransactions(),
                summary.getInitialDate(),
                summary.getFinalDate()
        ));
    }

    @GetMapping
    public ResponseEntity<List<SummaryResponseDto>> getMySummaries(
            @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(summaryService.getSummariesByUserId(loggedUser.getId())
                .stream()
                .map(s -> new SummaryResponseDto(
                        s.getId(),
                        s.getTotalIncome(),
                        s.getTotalExpense(),
                        s.getBalance(),
                        s.getTotalTransactions(),
                        s.getInitialDate(),
                        s.getFinalDate()
                )).toList());
    }
}
