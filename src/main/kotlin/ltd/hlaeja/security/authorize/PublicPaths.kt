package ltd.hlaeja.security.authorize

import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec

fun AuthorizeExchangeSpec.publicPaths(): AuthorizeExchangeSpec.Access = pathMatchers(
    "/favicon.ico",
    "/actuator/**",
    "/css/**",
    "/img/**",
    "/js/**",
    "/logout",
    "/login",
    "/",
)
