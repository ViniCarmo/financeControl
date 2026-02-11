package com.vinicius.finance_api.service;

import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.dto.UserResponseDto;
import com.vinicius.finance_api.entities.User;
import com.vinicius.finance_api.exceptions.EmailAlreadyExistsException;
import com.vinicius.finance_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User newUser = new User(null,
                userRequestDto.username(),
                userRequestDto.email(),
                userRequestDto.password(),
                null
        );
        userRepository.save(newUser);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail()
                )).toList();
    }

    public UserRequestDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new UserRequestDto(
                user.getUsername(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public void deleteUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public void updateUser(Integer id, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(userRequestDto.username());
        existingUser.setEmail(userRequestDto.email());
        existingUser.setPassword(userRequestDto.password());

        userRepository.save(existingUser);
    }
}
