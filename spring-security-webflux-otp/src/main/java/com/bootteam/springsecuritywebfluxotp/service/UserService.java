package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> register(UserPasswordDTO userPasswordDTO);

    Mono<User> checkCode(String code);
}
