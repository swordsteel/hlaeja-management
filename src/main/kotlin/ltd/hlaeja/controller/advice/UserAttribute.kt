package ltd.hlaeja.controller.advice

import kotlinx.coroutines.reactive.awaitFirstOrNull
import ltd.hlaeja.security.RemoteAuthentication
import ltd.hlaeja.security.user.GuestUser
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class UserAttribute : GuestUser {

    @ModelAttribute
    suspend fun remoteUser(model: Model) {
        model.addAttribute(
            "remoteUser",
            ReactiveSecurityContextHolder.getContext()
                .awaitFirstOrNull()
                ?.let { it.authentication as RemoteAuthentication }
                ?: guestUser(),
        )
    }
}
