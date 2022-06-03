package com.bootteam.springsecuritywebfluxotp.configuration;

import com.bootteam.springsecuritywebfluxotp.common.AppConstant;
import com.bootteam.springsecuritywebfluxotp.common.exception.CustomAccessDeniedHandler;
import com.bootteam.springsecuritywebfluxotp.common.exception.CustomAuthenticationEntryPoint;
import com.bootteam.springsecuritywebfluxotp.security.SecurityContextFilter;
import com.bootteam.springsecuritywebfluxotp.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@Slf4j
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private final String[] permitAllPatterns = new String[] {"/api/user/login","/api/user/register"};
    private final ReactiveUserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;


    public SecurityConfiguration(ReactiveUserDetailsService userDetailsService, TokenProvider tokenProvider, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(
                userDetailsService
        );
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
                .securityMatcher(new NegatedServerWebExchangeMatcher(new OrServerWebExchangeMatcher(
                        pathMatchers("/webjars/**","/app/**", "/i18n/**", "/content/**", "/swagger-ui/**", "/v3/api-docs/**", "/test/**"),
                        pathMatchers(HttpMethod.OPTIONS, "/**")
                )))
                .csrf()
                .disable()

                // Add JWT token filter
                .addFilterAt(new SecurityContextFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .authenticationManager(reactiveAuthenticationManager())

                // Set unauthorized requests exception handler
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .headers()
                .contentSecurityPolicy(AppConstant.DEFAULT_SRC_SELF_POLICY)
                .and()
                .referrerPolicy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .permissionsPolicy().policy(AppConstant.PERMISSION_POLICY)
                .and()
                .frameOptions().mode(Mode.DENY)
                .and()

                // Set permissions on endpoints
                .authorizeExchange()
                .pathMatchers(permitAllPatterns).permitAll()
                .pathMatchers("/api/user/**").authenticated();
        // @formatter:on
        return http.build();
    }
}
