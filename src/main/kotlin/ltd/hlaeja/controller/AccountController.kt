package ltd.hlaeja.controller

import java.util.UUID
import ltd.hlaeja.dto.Pagination
import ltd.hlaeja.exception.NoChangeException
import ltd.hlaeja.exception.NotFoundException
import ltd.hlaeja.exception.PasswordException
import ltd.hlaeja.exception.UsernameDuplicateException
import ltd.hlaeja.form.AccountForm
import ltd.hlaeja.service.AccountRegistryService
import ltd.hlaeja.util.toAccountForm
import ltd.hlaeja.util.toAccountRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

    @GetMapping("/edit-{account}")
    fun getEditAccount(
        @PathVariable account: UUID,
        model: Model,
    ): Mono<String> = accountRegistryService.getAccount(account)
        .doOnNext {
            model.addAttribute("account", account)
            model.addAttribute("accountForm", it.toAccountForm())
        }
        .then(Mono.just("account/edit"))

    @PostMapping("/edit-{account}")
    fun postEditAccount(
        @PathVariable account: UUID,
        @ModelAttribute("accountForm") accountForm: AccountForm,
        model: Model,
    ): Mono<String> = Mono.just(accountForm)
        .flatMap {
            accountRegistryService.updateAccount(
                account,
                it.toAccountRequest { password -> if (password.isNullOrEmpty()) null else password },
            )
        }
        .doOnNext {
            model.addAttribute("successMessage", "Saved changes!!!")
            model.addAttribute("account", account)
            model.addAttribute("accountForm", it.toAccountForm())
        }
        .then(Mono.just("account/edit"))
        .onErrorResume { error ->
            val errorMessage = when (error) {
                is NoChangeException -> Pair("successMessage", "No change to save")
                is NotFoundException -> Pair("errorMessage", "User dont exists. how did this happen?")
                is UsernameDuplicateException -> Pair("errorMessage", "Username already exists. Please choose another.")
                else -> Pair("errorMessage", "An unexpected error occurred. Please try again later.")
            }
            model.addAttribute(errorMessage.first, errorMessage.second)
            model.addAttribute("accountForm", accountForm)
            model.addAttribute("account", account)
            Mono.just("account/edit")
        }

    @GetMapping("/create")
    fun getCreateAccount(
        model: Model,
    ): Mono<String> = Mono.just("account/create")
        .doOnNext { model.addAttribute("accountForm", AccountForm("", "")) }

    @PostMapping("/create")
    fun postCreateAccount(
        @ModelAttribute("accountForm") accountForm: AccountForm,
        model: Model,
    ): Mono<String> = Mono.just(accountForm)
        .flatMap {
            accountRegistryService.addAccount(
                it.toAccountRequest { password ->
                    when {
                        password.isNullOrEmpty() -> throw PasswordException("Password requirements failed")
                        else -> password
                    }
                },
            )
        }
        .map { "redirect:/account" }
        .onErrorResume { error ->
            val errorMessage = when (error) {
                is UsernameDuplicateException -> "Username already exists. Please choose another."
                is PasswordException -> error.message
                else -> "An unexpected error occurred. Please try again later."
            }
            model.addAttribute("errorMessage", errorMessage)
            Mono.just("account/create")
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
