package ltd.hlaeja.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(serverHttpSecurity: ServerHttpSecurity): SecurityWebFilterChain = serverHttpSecurity
        .authorizeExchange(::authorizeExchange)
        .build()

    private fun authorizeExchange(authorizeExchange: AuthorizeExchangeSpec) = authorizeExchange
        .publicPaths().permitAll()
        .anyExchange().authenticated()

    private fun AuthorizeExchangeSpec.publicPaths(): AuthorizeExchangeSpec.Access = pathMatchers(
        "/css/**",
        "/js/**",
        "/img/**",
        "/actuator/**",
    )
}
