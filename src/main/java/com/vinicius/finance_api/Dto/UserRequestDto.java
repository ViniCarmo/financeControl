package com.vinicius.finance_api.Dto;

public record UserRequestDto(Integer id,
                             String username,
                             String email,
                             String password) {
}
