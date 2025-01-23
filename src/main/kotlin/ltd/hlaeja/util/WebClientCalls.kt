package ltd.hlaeja.util

import ltd.hlaeja.library.accountRegistry.Account
import ltd.hlaeja.library.accountRegistry.Authentication
import ltd.hlaeja.property.AccountRegistryProperty
import org.springframework.http.HttpStatus.LOCKED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun WebClient.accountRegistryAuthenticate(
    request: Authentication.Request,
    property: AccountRegistryProperty,
): Mono<Authentication.Response> = post()
    .uri("${property.url}/authenticate".also(::logCall))
    .bodyValue(request)
    .retrieve()
    .onStatus(LOCKED::equals) { throw LockedException("Account is locked") }
    .onStatus(UNAUTHORIZED::equals) { throw BadCredentialsException("Invalid credentials") }
    .onStatus(NOT_FOUND::equals) { throw UsernameNotFoundException("User not found") }
    .bodyToMono(Authentication.Response::class.java)

fun WebClient.accountRegistryAccounts(
    page: Int,
    size: Int,
    property: AccountRegistryProperty
): Flux<Account.Response> = get()
    .uri("${property.url}/accounts?page=$page&size=$size".also(::logCall))
    .retrieve()
    .bodyToFlux(Account.Response::class.java)
