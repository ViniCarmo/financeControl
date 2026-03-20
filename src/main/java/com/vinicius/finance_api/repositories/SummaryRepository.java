package com.vinicius.finance_api.repositories;

import com.vinicius.finance_api.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SummaryRepository extends JpaRepository<Summary, Integer> {

    List<Summary> findByUserId(Integer userId);

    boolean existsByUserIdAndInitialDate(Integer userId, LocalDate initialDate);

    Optional<Summary> findByIdAndUserId(Integer id, Integer userId);
}
