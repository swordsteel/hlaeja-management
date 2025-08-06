package ltd.hlaeja.security.manager

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import ltd.hlaeja.jwt.service.PublicJwtService
import ltd.hlaeja.service.AccountRegistryService
import ltd.hlaeja.util.RemoteAuthenticationUtil
import ltd.hlaeja.util.toAuthenticationRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
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
        .doOnNext { publisher.publishEvent(AuthenticationSuccessEvent(it)) }
        .doOnError { ex ->
            if (ex is AuthenticationException) {
                publisher.publishEvent(AuthenticationFailureBadCredentialsEvent(authentication, ex))
            }
        }

    private fun processToken(
        response: ltd.hlaeja.library.accountRegistry.Authentication.Response,
    ): Authentication = try {
        publicJwtService.verify(response.token) { claims -> RemoteAuthenticationUtil.fromJwtClaims(claims) }
    } catch (e: JwtException) {
        throw "An error occurred while processing token: ${e.message}"
            .let {
                log.error(e) { it }
                AuthenticationServiceException(it, e)
            }
    }
}
