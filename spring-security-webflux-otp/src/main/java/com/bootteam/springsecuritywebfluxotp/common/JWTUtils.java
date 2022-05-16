package com.bootteam.springsecuritywebfluxotp.common;

import lombok.experimental.UtilityClass;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;


@UtilityClass
public class JWTUtils {

    private final String TOKEN_PREFIX = "Bearer ";
    public String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(AppConstant.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
