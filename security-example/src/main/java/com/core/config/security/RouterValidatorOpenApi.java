package com.core.config.security;

public final class RouterValidatorOpenApi {

    String[] openApis =
            {
                    "/api/auth/signup",
                    "/api/auth/signin",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/v2/api-docs/**",
                    "/swagger-resources/**"
            };
}
