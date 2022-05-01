package com.bootteam.springsecuritywebfluxotp.common.exception;

import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class BaseControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex){
        LOGGER.error("Exception caught in handleRequestBodyError :  {} " ,ex.getMessage(),  ex);
        var error = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(","));
        LOGGER.error("errorList : {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({EmailAlreadyUsedException.class,UsernameAlreadyUsedException.class,})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiResponseDTO> noDataFoundException(Exception ex, ServerWebExchange exchange) {
        LOGGER.debug(ex.getMessage(), ex.getCause());
        return Mono.just(new ApiResponseDTO(null, ex.getMessage()));
    }

}
