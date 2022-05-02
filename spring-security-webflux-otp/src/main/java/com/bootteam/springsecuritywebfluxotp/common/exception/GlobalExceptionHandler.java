package com.bootteam.springsecuritywebfluxotp.common.exception;

import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.ApiResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerErrorException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final List<ExceptionRule> exceptionsRules = List.of(
            new ExceptionRule(OtpCheckingException.class, HttpStatus.UNAUTHORIZED, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.UNAUTHORIZED)),
            new ExceptionRule(ExpiredJwtException.class, HttpStatus.UNAUTHORIZED, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.UNAUTHORIZED)),
            new ExceptionRule(BadCredentialsException.class, HttpStatus.UNAUTHORIZED, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.UNAUTHORIZED)),
            new ExceptionRule(ValidatorException.class, HttpStatus.BAD_REQUEST, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.BAD_REQUEST)),
            new ExceptionRule(EmailAlreadyUsedException.class, HttpStatus.NOT_FOUND, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.NOT_FOUND)),
            new ExceptionRule(UsernameAlreadyUsedException.class, HttpStatus.NOT_FOUND, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.NOT_FOUND)),
            new ExceptionRule(MethodNotAllowedException.class, HttpStatus.METHOD_NOT_ALLOWED, (Throwable throwable, HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.METHOD_NOT_ALLOWED)),
            new ExceptionRule(ServerErrorException.class, HttpStatus.INTERNAL_SERVER_ERROR, (Throwable throwable,  HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.INTERNAL_SERVER_ERROR)),
            new ExceptionRule(NotAcceptableStatusException.class, HttpStatus.NOT_ACCEPTABLE, (Throwable throwable,  HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.NOT_ACCEPTABLE)),
            new ExceptionRule(UnsupportedMediaTypeStatusException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE, (Throwable throwable,  HttpStatus httpStatus) -> getServerResponseMono(throwable, HttpStatus.UNSUPPORTED_MEDIA_TYPE))
    );

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.setMessageReaders(configurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);

        return exceptionsRules.stream()
                .map(exceptionRule -> exceptionRule.getResponse(error))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> getServerResponseMono(error, null));
    }

    /**
     * @param error Throwable error
     * @param httpStatus response status, if null use 404 not found
     * @return Mono of ServerResponse
     */
    private Mono<ServerResponse> getServerResponseMono(Throwable error, HttpStatus httpStatus) {
        return ServerResponse.status(Objects.requireNonNullElse(httpStatus, HttpStatus.BAD_REQUEST)).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ApiResponseDTO(null, error.getMessage()));
    }
}