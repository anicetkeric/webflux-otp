package com.bootteam.springsecuritywebfluxotp.web.router;


import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.ApiResponseDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.LoginDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import com.bootteam.springsecuritywebfluxotp.web.handler.AccountHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class AccountRouter {



    @RouterOperations({
            @RouterOperation(path = "/api/user/authenticate", produces = {MediaType.APPLICATION_JSON_VALUE },
                    beanClass = AccountHandler.class,  beanMethod = "isAuthenticated",
                    operation = @Operation(operationId = "isAuthenticated",
                            responses = {
                            @ApiResponse(responseCode = "200", description = "get current user.", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class)))
                    })
            ),
            @RouterOperation(path = "/api/user/register", produces = {MediaType.APPLICATION_JSON_VALUE },
                    beanClass = AccountHandler.class,  beanMethod = "register",
                    operation = @Operation(operationId = "register",
                            responses = {
                            @ApiResponse(responseCode = "201", description = "User account created.", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "404", description = "User account already exist", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                    }, requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserPasswordDTO.class )))
                    )
            ),
            @RouterOperation(path = "/api/user/login", produces = {MediaType.APPLICATION_JSON_VALUE },
                    beanClass = AccountHandler.class,  beanMethod = "login",
                    operation = @Operation(operationId = "login",
                            responses = {
                            @ApiResponse(responseCode = "200", description = "User account created.", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "404", description = "User account already exist", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                    }, requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = LoginDTO.class )))
                    )
            ),
            @RouterOperation(path = "/api/user/otp/{code}", produces = {MediaType.APPLICATION_JSON_VALUE },
                    beanClass = AccountHandler.class,  beanMethod = "optCheckCode",
                    operation = @Operation(operationId = "optCheckCode",
                            responses = {
                            @ApiResponse(responseCode = "200", description = "OTP success.", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "404", description = "User account not found", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))),
                            @ApiResponse(responseCode = "500", description = "Something went wrong", content = @Content(schema = @Schema(implementation = ApiResponseDTO.class)))
                    }, parameters = {@Parameter(in = ParameterIn.PATH, name = "code") }
                    )
            ),
    })
    @Bean
    public RouterFunction<ServerResponse> routeUserAccount(final AccountHandler accountHandler) {

        return route()
                .nest(path("/api/user"), builder ->
                        builder
                                .GET("/authenticate", accountHandler::isAuthenticated)
                                .GET("/resend/code", accountHandler::optResendCode)
                                .POST("/register", accountHandler::register)
                                .POST("/login", accountHandler::login)
                                .GET("/otp/{code}", accountHandler::optCheckCode))
                .build();
    }

}