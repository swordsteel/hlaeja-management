package ltd.hlaeja.security.handler

import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class UserAccessDeniedHandler : ServerAccessDeniedHandler {
    override fun handle(
        exchange: ServerWebExchange,
        denied: AccessDeniedException,
    ): Mono<Void> = Mono.error(ResponseStatusException(NOT_FOUND, "Access denied ${exchange.request.path}", denied))
}
