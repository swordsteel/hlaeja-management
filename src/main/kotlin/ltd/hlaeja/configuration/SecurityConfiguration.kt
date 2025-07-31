package ltd.hlaeja.configuration

import ltd.hlaeja.security.handler.CsrfAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.FOUND
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(serverHttpSecurity: ServerHttpSecurity): SecurityWebFilterChain = serverHttpSecurity
        .csrf { it.accessDeniedHandler(CsrfAccessDeniedHandler()) }
        .authorizeExchange(::authorizeExchange)
        .formLogin(::formLogin)
        .logout(::logout)
        .build()

    private fun logout(logout: ServerHttpSecurity.LogoutSpec) = logout.logoutUrl("/logout")
        .logoutSuccessHandler { webFilter, _ ->
            webFilter.exchange.response.headers.add("Location", "/logout")
            webFilter.exchange.response.statusCode = FOUND
            webFilter.exchange.response.setComplete()
        }

    private fun formLogin(login: FormLoginSpec) = login.loginPage("/login")

    private fun authorizeExchange(authorizeExchange: AuthorizeExchangeSpec) = authorizeExchange
        .publicPaths().permitAll()
        .adminPaths().hasRole("ADMIN")
        .anyExchange().authenticated()

    private fun AuthorizeExchangeSpec.adminPaths(): AuthorizeExchangeSpec.Access = pathMatchers(
        "/account/**",
        "/type/**",
    )

    private fun AuthorizeExchangeSpec.publicPaths(): AuthorizeExchangeSpec.Access = pathMatchers(
        "/css/**",
        "/js/**",
        "/img/**",
        "/actuator/**",
        "/login",
        "/logout",
        "/",
    )
}
