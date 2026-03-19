package com.vinicius.finance_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.entity.User;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return 201 when user is created successfully")
    void createUser_ShouldReturn201_WhenDataIsValid() throws Exception {
        UserRequestDto dto = new UserRequestDto("Vinicius", "vinicius@gmail.com", "123456");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return 409 when email already exists")
    void createUser_ShouldReturn409_WhenEmailAlreadyExists() throws Exception {
        User user = new User();
        user.setUsername("Vinicius");
        user.setEmail("vinicius@gmail.com");
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);

        UserRequestDto dto = new UserRequestDto("Outro", "vinicius@gmail.com", "123456");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }
}