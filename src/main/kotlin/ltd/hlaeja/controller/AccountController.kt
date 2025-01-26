package ltd.hlaeja.controller

import ltd.hlaeja.dto.Pagination
import ltd.hlaeja.service.AccountRegistryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/account")
class AccountController(
    private val accountRegistryService: AccountRegistryService,
) {
    companion object {
        const val DEFAULT_PAGE: Int = 1
        const val DEFAULT_SIZE: Int = 25
    }

    @GetMapping
    fun getDefaultAccounts(
        model: Model,
    ): Mono<String> = getAccounts(DEFAULT_PAGE, DEFAULT_SIZE, model)

    @GetMapping("/page-{page}")
    fun getAccountsPage(
        @PathVariable page: Int,
        model: Model,
    ): Mono<String> = getAccounts(page, DEFAULT_SIZE, model)

    @GetMapping("/page-{page}/show-{size}")
    fun getAccountsPageSize(
        @PathVariable page: Int,
        @PathVariable size: Int,
        model: Model,
    ): Mono<String> = getAccounts(page, size, model)

    private fun getAccounts(
        page: Int,
        size: Int,
        model: Model,
    ) = accountRegistryService.getAccounts(page, size)
        .collectList()
        .doOnNext { items ->
            model.addAttribute("items", items)
            model.addAttribute("pagination", Pagination(page, size, items.size, DEFAULT_SIZE))
        }
        .then(Mono.just("account/users"))
}

