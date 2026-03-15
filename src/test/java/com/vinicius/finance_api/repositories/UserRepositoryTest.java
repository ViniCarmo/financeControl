package com.vinicius.finance_api.repositories;

import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deve salvar um usuário no banco de dados")
    void findByEmail() {
    }

}