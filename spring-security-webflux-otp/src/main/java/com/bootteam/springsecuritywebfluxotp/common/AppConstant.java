package com.bootteam.springsecuritywebfluxotp.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstant {

    public final String AUTHORIZATION_HEADER = "Authorization";

    public final String AUTHORITIES_KEY = "auth";

    public final String TOKEN_SECRET = "ZHnwRUs+l7aWVqYV76MRDs6Iu7vIvQpwNtkBaB3cYmCWuTGt41nyuV6XAYDP+imPJG1SRPC/cZEqAefr1qyOkA==";

    public final long TOKEN_VALIDITY_TIME = 1000 * 86400;

}
