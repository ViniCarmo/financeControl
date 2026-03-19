package com.vinicius.finance_api.infra.security.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vinicius.finance_api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

        @InjectMocks
        private TokenService tokenService;

        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(tokenService, "secret", "test-secret-key");
        }

        @Test
        @DisplayName("Should generate token with user email as object")
        void generateToken_ShouldReturnTokenWithEmailAsSubject() {
            User user = new User();
            user.setEmail("vinicarmo@gmail.com");

            String token = tokenService.generateToken(user);
            String subject = tokenService.getSubject(token);
            assertThat(subject).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Should throw exception for invalid token")
    void getSubject_ShouldThrowException_WhenTokenIsInvalid() {
        assertThrows(JWTVerificationException.class,
                () -> tokenService.getSubject("token.invalido.aqui"));
    }
    }
