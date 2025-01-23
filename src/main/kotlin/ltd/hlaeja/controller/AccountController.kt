package ltd.hlaeja.controller

import ltd.hlaeja.service.AccountRegistryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/account")
class AccountController(
    private val accountRegistryService: AccountRegistryService,
) {

    @GetMapping
    fun getAccounts(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "2") size: Int,
        model: Model,
    ): Mono<String> = accountRegistryService.getAccounts(page, size)
        .collectList()
        .doOnNext { items ->
            model.addAttribute("items", items)
            model.addAttribute("page", page)
            model.addAttribute("size", size)
        }
        .then(Mono.just("account/users"))
}
