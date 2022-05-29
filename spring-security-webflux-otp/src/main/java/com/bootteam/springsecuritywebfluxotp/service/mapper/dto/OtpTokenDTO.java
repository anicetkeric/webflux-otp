package com.bootteam.springsecuritywebfluxotp.service.mapper.dto;

import com.bootteam.springsecuritywebfluxotp.domain.document.User;

public record OtpTokenDTO(String token, User user) {}
