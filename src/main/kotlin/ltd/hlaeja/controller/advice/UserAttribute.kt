package ltd.hlaeja.controller.advice

import java.util.UUID
import kotlinx.coroutines.reactive.awaitFirstOrNull
import ltd.hlaeja.security.RemoteAuthentication
import ltd.hlaeja.security.RemoteUserDetail
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class UserAttribute {

    @ModelAttribute
    suspend fun remoteUser(model: Model) {
        val remoteAuthentication: RemoteAuthentication = ReactiveSecurityContextHolder.getContext()
            .awaitFirstOrNull()
            ?.let { it.authentication as RemoteAuthentication }
            ?: RemoteAuthentication(
                RemoteUserDetail(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    "n/a",
                ),
                mutableListOf(),
            )
        model.addAttribute("remoteUser", remoteAuthentication)
    }
}
