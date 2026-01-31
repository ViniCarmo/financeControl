package com.vinicius.finance_api.Controller;

import com.vinicius.finance_api.Dto.UserRequestDto;
import com.vinicius.finance_api.Dto.UserResponseDto;
import com.vinicius.finance_api.Service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
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

