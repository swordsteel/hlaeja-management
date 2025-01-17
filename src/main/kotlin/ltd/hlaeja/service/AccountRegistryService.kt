package ltd.hlaeja.service

import io.github.oshai.kotlinlogging.KotlinLogging
import ltd.hlaeja.library.accountRegistry.Authentication
import ltd.hlaeja.property.AccountRegistryProperty
import ltd.hlaeja.util.accountRegistryAuthenticate
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

@Service
class AccountRegistryService(
    private val webClient: WebClient,
    private val property: AccountRegistryProperty,
) {

    fun authenticate(
        request: Authentication.Request,
    ): Mono<Authentication.Response> = webClient.accountRegistryAuthenticate(request, property)
        .onErrorResume { error ->
            when (error) {
                is AuthenticationException -> Mono.error(error)

                is WebClientResponseException -> "WebClient response exception: ${error.message}".let {
                    log.error(error) { it }
                    Mono.error(AuthenticationServiceException(it, error))
                }

                is WebClientRequestException -> "An error occurred while making a request: ${error.message}".let {
                    log.error(error) { it }
                    Mono.error(AuthenticationServiceException(it, error))
                }

                else -> "An unexpected error occurred: ${error.message}".let {
                    log.error(error) { it }
                    Mono.error(AuthenticationServiceException(it, error))
                }
            }
        }
}
