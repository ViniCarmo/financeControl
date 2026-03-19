package com.vinicius.finance_api.service;

import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.infra.exceptions.EmailAlreadyExistsException;
import com.vinicius.finance_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("Should create a new user successfully")
    void createUser() {
        UserRequestDto user = new UserRequestDto("Vinicius", "vinicius@gmail.com", "123456");

        when(userRepository.findByEmail(user.email())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(user.password())).thenReturn("encodedPassword");

        userService.createUser(user);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw an exception when trying to create a user with an existing email")
    void createUserfailed_duplicateEmail() {
        UserRequestDto user = new UserRequestDto("Vinicius", "vinicius@gmail.com", "123456");

        when(userRepository.findByEmail(user.email())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(user));

        verify(userRepository, never()).save(any(User.class));


    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUserById_ShouldDeleteUser_WhenUserExists() {
        Integer id = 1;
        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUserById(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when user not found on delete")
    void deleteUserById_ShouldThrowException_WhenUserNotFound() {
        Integer id = 1;
        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> userService.deleteUserById(id));

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_ShouldUpdateUser_WhenUserExists() {
        Integer id = 1;
        UserRequestDto dto = new UserRequestDto("NovoNome", "novo@gmail.com", "novaSenha");
        User existingUser = new User(id, "Vinicius", "vinicius@gmail.com", "senhaAntiga", null);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(dto.password())).thenReturn("encodedNovaSenha");

        userService.updateUser(id, dto);

        verify(userRepository, times(1)).save(existingUser);
        assertThat(existingUser.getEmail()).isEqualTo("novo@gmail.com");
        assertThat(existingUser.getPassword()).isEqualTo("encodedNovaSenha");
    }

    @Test
    @DisplayName("Should throw exception when user not found on update")
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        Integer id = 1;
        UserRequestDto dto = new UserRequestDto("NovoNome", "novo@gmail.com", "novaSenha");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.updateUser(id, dto));

        verify(userRepository, never()).save(any());
    }
}