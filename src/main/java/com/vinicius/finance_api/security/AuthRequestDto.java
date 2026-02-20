package com.vinicius.finance_api.security;

public record AuthRequestDto(

        String login,

        String password) {
}
