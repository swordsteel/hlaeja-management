package ltd.hlaeja.controller.advice

import kotlinx.coroutines.runBlocking
import ltd.hlaeja.security.user.RemoteAuthentication
import ltd.hlaeja.security.user.GuestUser
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitSession

@Component
class ErrorAttributes : DefaultErrorAttributes(), GuestUser {

    override fun getErrorAttributes(
        serverRequest: ServerRequest,
        errorAttributeOptions: ErrorAttributeOptions,
    ): MutableMap<String, Any> =
        super.getErrorAttributes(serverRequest, errorAttributeOptions)
            .also { attribute ->
                attribute["remoteUser"] = runBlocking {
                    serverRequest.awaitSession()
                        .attributes["SPRING_SECURITY_CONTEXT"]
                        ?.let { context -> (context as SecurityContextImpl).authentication }
                        ?: guestUser()
                } as RemoteAuthentication
            }
}
