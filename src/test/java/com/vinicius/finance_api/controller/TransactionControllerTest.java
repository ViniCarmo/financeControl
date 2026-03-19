package com.vinicius.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.finance_api.dto.TransactionRequestDto;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.enums.TransactionType;
import com.vinicius.finance_api.infra.security.service.TokenService;
import com.vinicius.finance_api.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private String token;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("Vinicius");
        user.setEmail("vinicius@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);

        token = tokenService.generateToken(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 403 when no token is provided")
    void getTransactions_ShouldReturn403_WhenNoToken() throws Exception {
        mockMvc.perform(get("/transactions"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 200 when token is valid")
    void getTransactions_ShouldReturn200_WhenTokenIsValid() throws Exception {
        mockMvc.perform(get("/transactions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 201 when transaction is created successfully")
    void saveTransaction_ShouldReturn201_WhenTokenIsValid() throws Exception {
        TransactionRequestDto dto = new TransactionRequestDto(
                100.0, TransactionType.INCOME, LocalDate.now(), "Salário");

        mockMvc.perform(post("/transactions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }
}