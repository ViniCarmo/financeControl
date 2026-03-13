package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.dto.UserRequestDto;
import com.vinicius.finance_api.dto.UserResponseDto;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getme(@AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(new UserResponseDto(
                loggedUser.getId(),
                loggedUser.getUsername(),
                loggedUser.getEmail()
        ));
    }


    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserById(@AuthenticationPrincipal User loggedUser) {
        userService.deleteUserById(loggedUser.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal User loggeduser, @RequestBody @Valid UserRequestDto userRequestDto) {
        userService.updateUser(loggeduser.getId(), userRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
    }

}

