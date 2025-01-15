package ltd.hlaeja.controller

import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import reactor.core.publisher.Mono

@Controller
class AuthenticationController {

    @GetMapping("/login")
    @ResponseStatus(UNAUTHORIZED)
    fun login(): Mono<String> = Mono.just("authentication/login")

    @GetMapping("/logout")
    fun logout(): Mono<String> = ReactiveSecurityContextHolder.getContext()
        .filter { it.authentication?.isAuthenticated == true }
        .map { "authentication/logout" }
        .defaultIfEmpty("authentication/goodbye")
}
