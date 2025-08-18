package ltd.hlaeja.security.authorize

import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec

fun AuthorizeExchangeSpec.adminPaths(): AuthorizeExchangeSpec.Access = pathMatchers(
    "/accounts/**",
    "/typse/**",
    "/devices/**",
    "/nodes/**",
)
