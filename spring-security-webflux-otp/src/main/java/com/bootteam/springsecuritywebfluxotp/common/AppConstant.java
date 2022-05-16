package com.bootteam.springsecuritywebfluxotp.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstant {

    public final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CODE_KEY = "code";

    public static final String MESSAGE_KEY = "message";
    public final String AUTHORITIES_KEY = "auth";

    public final String TOKEN_SECRET = "ZHnwRUs+l7aWVqYV76MRDs6Iu7vIvQpwNtkBaB3cYmCWuTGt41nyuV6XAYDP+imPJG1SRPC/cZEqAefr1qyOkA==";

    // 24h Hours to Milliseconds = 86400000 ms
    public final long TOKEN_VALIDITY_TIME = 1_000L*86400;

    // temp token validity = 10min
    public final long TOKEN_TEMP_VALIDITY_TIME = 1000L*600;

}
