package com.vinicius.finance_api.Repositories;

import com.vinicius.finance_api.Entities.MonthlySummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SumaryRepository extends JpaRepository<MonthlySummary, Integer> {
}
