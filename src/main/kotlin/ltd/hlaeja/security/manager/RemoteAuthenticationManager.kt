package ltd.hlaeja.security.manager

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import java.util.UUID
import ltd.hlaeja.jwt.service.PublicJwtService
import ltd.hlaeja.security.user.RemoteAuthentication
import ltd.hlaeja.security.user.RemoteUserDetail
import ltd.hlaeja.service.AccountRegistryService
import ltd.hlaeja.util.toAuthenticationRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

@Component
class RemoteAuthenticationManager(
    private val accountRegistryService: AccountRegistryService,
    private val publicJwtService: PublicJwtService,
    private val publisher: ApplicationEventPublisher,
) : ReactiveAuthenticationManager {

    override fun authenticate(
        authentication: Authentication,
    ): Mono<Authentication> = accountRegistryService.authenticate(authentication.toAuthenticationRequest())
        .map(::processToken)
        .doOnNext { publisher.publishEvent(AuthenticationSuccessEvent(it))  }
        .doOnError { ex ->
            if (ex is AuthenticationException) {
                publisher.publishEvent(AuthenticationFailureBadCredentialsEvent(authentication, ex))
            }
        }

    private fun processToken(
        response: ltd.hlaeja.library.accountRegistry.Authentication.Response,
    ): Authentication = try {
        publicJwtService.verify(response.token) { claims -> makeRemoteAuthentication(claims) }
    } catch (e: JwtException) {
        throw "An error occurred while processing token: ${e.message}"
            .let {
                log.error(e) { it }
                AuthenticationServiceException(it, e)
            }
    }

    private fun makeRemoteAuthentication(
        claims: Jws<Claims>,
    ) = RemoteAuthentication(
        makeRemoteUserDetail(claims),
        makeSimpleGrantedAuthorities(claims),
        true,
    )

    private fun makeSimpleGrantedAuthorities(
        claims: Jws<Claims>,
    ): MutableList<SimpleGrantedAuthority> = (claims.payload["role"] as String)
        .split(",")
        .map { SimpleGrantedAuthority("ROLE_$it") }
        .toMutableList()

    private fun makeRemoteUserDetail(
        claims: Jws<Claims>,
    ): RemoteUserDetail = RemoteUserDetail(
        UUID.fromString(claims.payload["id"] as String),
        claims.payload["username"] as String,
    )
}
