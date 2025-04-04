package ltd.hlaeja.util

import io.github.oshai.kotlinlogging.KotlinLogging
import ltd.hlaeja.exception.HlaejaException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

fun logCall(url: String) = log.debug { "calling: $url" }

fun <R> hlaejaErrorHandler(
    error: Throwable,
): Mono<out R> = when (error) {
    is HlaejaException -> Mono.error(error)
    else -> Mono.error(ResponseStatusException(BAD_REQUEST, error.message))
}

fun <R> responseErrorHandler(
    error: Throwable,
): Mono<out R> = when (error) {
    is ResponseStatusException -> Mono.error(error)
    else -> Mono.error(ResponseStatusException(BAD_REQUEST, error.message))
}
