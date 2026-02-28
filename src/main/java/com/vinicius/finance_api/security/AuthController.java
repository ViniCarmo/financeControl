package com.vinicius.finance_api.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        var token = new UsernamePasswordAuthenticationToken(
                authRequestDto.email(),
                authRequestDto.password()
        );
        var auth = authenticationManager.authenticate(token);
        return ResponseEntity.ok().build();
    }

}
