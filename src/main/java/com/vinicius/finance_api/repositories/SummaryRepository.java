package com.vinicius.finance_api.repositories;

import com.vinicius.finance_api.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {

    List<Summary> findByUserId(Integer userId);
}
