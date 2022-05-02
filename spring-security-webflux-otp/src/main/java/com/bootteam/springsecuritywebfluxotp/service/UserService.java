package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.OtpTokenDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

public interface UserService {

    Mono<User> createUser(UserPasswordDTO userPasswordDTO);

    Mono<String> checkCode(String authenticationName, @NotNull String code);

    Mono<OtpTokenDTO> setUserOtp(Authentication authentication);
}
