package ltd.hlaeja.controller

import ltd.hlaeja.security.RemoteUserDetail
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@Controller
class HomeController {

    @GetMapping("/")
    fun home(model: Model): Mono<String> = ReactiveSecurityContextHolder.getContext()
        .filter { it.authentication?.isAuthenticated == true }
        .map {
            (it.authentication.principal as RemoteUserDetail).let { user ->
                model.addAttribute("id", user.id)
                model.addAttribute("username", user.username)
            }
            "home/welcome"
        }
        .defaultIfEmpty("home/index")
}
