package com.vinicius.finance_api.service;

import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.dto.UserResponseDto;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.exceptions.EmailAlreadyExistsException;
import com.vinicius.finance_api.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByEmail(userRequestDto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User newUser = new User(null,
                userRequestDto.username(),
                userRequestDto.email(),
                passwordEncoder.encode(userRequestDto.password()),
                null
        );
        userRepository.save(newUser);
    }

    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        ));
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
        existingUser.setPassword(passwordEncoder.encode(userRequestDto.password()));

        userRepository.save(existingUser);
    }
}
