package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.entities.User;
import com.vinicius.finance_api.security.AuthRequestDto;
import com.vinicius.finance_api.security.DadosTokenJWT;
import com.vinicius.finance_api.security.TokenService;
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

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        var authtoken = new UsernamePasswordAuthenticationToken(
                authRequestDto.email(),
                authRequestDto.password());
        var auth = authenticationManager.authenticate(authtoken);

        var tokenJWT = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }























}
