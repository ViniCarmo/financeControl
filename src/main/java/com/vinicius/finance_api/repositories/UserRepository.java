package com.vinicius.finance_api.repositories;

import com.vinicius.finance_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    UserDetails findByLogin(String login);
}
