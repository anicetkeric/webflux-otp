package com.bootteam.springsecuritywebfluxotp.security;

import com.bootteam.springsecuritywebfluxotp.common.AppConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Component
public class TokenProvider {

    private final Key key;

    private final JwtParser jwtParser;

    public TokenProvider() {
        key = Keys.hmacShaKeyFor(AppConstant.TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String generateToken(Authentication authentication, boolean isTempToken) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + (isTempToken ? AppConstant.TOKEN_TEMP_VALIDITY_TIME : AppConstant.TOKEN_VALIDITY_TIME));


        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AppConstant.AUTHORITIES_KEY, authorities)
                .setId(UUID.randomUUID().toString())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .setIssuedAt(Date.from(Instant.now()))
                .serializeToJsonWith(new JacksonSerializer())
                .compact();

    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AppConstant.AUTHORITIES_KEY).toString().split(","))
            .filter(auth -> !auth.trim().isEmpty())
            .map(SimpleGrantedAuthority::new)
            .toList();

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);

            return true;
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            LOGGER.trace("Invalid JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Token validation error {}", e.getMessage());
        }

        return false;
    }

    public Mono<Authentication> getCurrentUserAuthentication() {
        return ReactiveSecurityContextHolder
                .getContext()
                .map(SecurityContext::getAuthentication);
    }

}
