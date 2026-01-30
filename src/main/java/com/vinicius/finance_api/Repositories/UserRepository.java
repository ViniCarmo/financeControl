package com.vinicius.finance_api.Repositories;

import com.vinicius.finance_api.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
