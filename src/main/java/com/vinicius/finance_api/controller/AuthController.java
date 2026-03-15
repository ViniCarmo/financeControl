package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.dto.AuthRequestDto;
import com.vinicius.finance_api.dto.TokenDataJWT;
import com.vinicius.finance_api.infra.security.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        var authtoken = new UsernamePasswordAuthenticationToken(
                authRequestDto.email(),
                authRequestDto.password());
        var auth = authenticationManager.authenticate(authtoken);

        var tokenJWT = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new TokenDataJWT(tokenJWT));
    }























}
