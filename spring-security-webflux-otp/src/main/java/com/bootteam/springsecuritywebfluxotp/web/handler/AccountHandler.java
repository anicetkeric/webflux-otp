package com.bootteam.springsecuritywebfluxotp.web.handler;


import com.bootteam.springsecuritywebfluxotp.security.SecurityUtils;
import com.bootteam.springsecuritywebfluxotp.security.TokenProvider;
import com.bootteam.springsecuritywebfluxotp.service.UserService;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.ApiResponseDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.LoginDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    public Mono<ServerResponse> isAuthenticated(ServerRequest serverRequest) {

        return serverRequest.principal()
                .map(Principal::getName)
                .flatMap(user ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(user, "Current user is authenticated")));
    }

    public Mono<ServerResponse> optCheckCode(ServerRequest serverRequest) {

        var otpCode = serverRequest.pathVariable("code");

        return SecurityUtils
                .getCurrentUserLogin()
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Current user login not found")))
                .flatMap(u -> userService.checkCode(otpCode))
                .flatMap(user ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(user, "OtpCode")));
    }


    public Mono<ServerResponse> login(final ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
                .flatMap(login ->
                        authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                                .flatMap(auth -> Mono.fromCallable(() -> tokenProvider.generateToken(auth)))
                )
                .flatMap(jwt ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(jwt, "User login success")));
    }

    public Mono<ServerResponse> register(final ServerRequest request) {
        return request.bodyToMono(UserPasswordDTO.class)
                .flatMap(userService::register)
                .flatMap(savedUser ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .bodyValue(new ApiResponseDTO(savedUser, "User created successfully")));
    }

}