package com.bootteam.springsecuritywebfluxotp.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ExceptionRule {

    Class<?> exceptionClass;

    HttpStatus httpStatus;
    BiFunction<Throwable, HttpStatus, Mono<ServerResponse>> triFunction;

    private boolean supports(Throwable error) {
        return exceptionClass.isInstance(error);
    }

    Mono<ServerResponse> getResponse(Throwable error) {
        return supports(error) ? triFunction.apply(error, httpStatus) : null;
    }
}