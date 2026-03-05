package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.dto.UserResponseDto;
import com.vinicius.finance_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequestDto> getUserById(@PathVariable Integer id) {
        UserRequestDto userRequestDto = userService.getUserById(id);
        return ResponseEntity.ok(userRequestDto);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRequestDto userRequestDto) {
        userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok().build();
    }
}

