package com.bootteam.springsecuritywebfluxotp.web.handler;


import com.bootteam.springsecuritywebfluxotp.common.exception.ValidatorException;
import com.bootteam.springsecuritywebfluxotp.service.UserService;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.ApiResponseDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.LoginDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.security.Principal;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final UserService userService;
    private final Validator validator;
    private final ReactiveAuthenticationManager authenticationManager;

    @PreAuthorize("hasRole('ROLE_USER') AND hasRole('ROLE_ADMIN')")
    public Mono<ServerResponse> isAuthenticated(ServerRequest serverRequest) {

        return serverRequest.principal()
                .map(Principal::getName)
                .flatMap(user ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(user, "Current user is authenticated")));
    }

    @PreAuthorize("hasRole('PRE_AUTH')")
    public Mono<ServerResponse> optCheckCode(ServerRequest serverRequest) {

        var otpCode = serverRequest.pathVariable("code");

        return serverRequest.principal()
                .map(Principal::getName)
                .flatMap(u -> userService.checkCode(u, otpCode))
                .flatMap(token ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(token, "Otp checking success")));
    }

    @PreAuthorize("hasRole('PRE_AUTH')")
    public Mono<ServerResponse> optResendCode(ServerRequest serverRequest) {

        return serverRequest.principal()
                .map(Principal::getName)
                .flatMap(userService::resendCode)
                .flatMap(token ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(token, "Otp checking success")));
    }


    public Mono<ServerResponse> login(final ServerRequest request) {
        return request.bodyToMono(LoginDTO.class)
                .flatMap(body ->
                        validator.validate(body).isEmpty()
                                ? Mono.just(body)
                                : Mono.error(new ValidatorException(validator.validate(body).stream().map(ConstraintViolation::getMessage).toList().toString()))
                )
                .flatMap(login ->
                        authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()))
                                .flatMap(userService::setUserOtp)
                )
                .flatMap(jwt ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(new ApiResponseDTO(jwt.token(), "Partially successful user login - an OTP code has been sent to your email address")));
    }

    public Mono<ServerResponse> register(final ServerRequest request) {

        return request.bodyToMono(UserPasswordDTO.class)
                .flatMap(body ->
                        validator.validate(body).isEmpty()
                        ? Mono.just(body)
                        : Mono.error(new ValidatorException(validator.validate(body).stream().map(ConstraintViolation::getMessage).toList().toString()))
                )
                .flatMap(userService::createUser)
                .flatMap(savedUser ->
                        ServerResponse.status(HttpStatus.CREATED)
                                .bodyValue(new ApiResponseDTO(savedUser, "User created successfully")));
    }

}