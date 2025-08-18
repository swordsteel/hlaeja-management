package ltd.hlaeja.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.util.UUID
import ltd.hlaeja.exception.NoChangeException
import ltd.hlaeja.exception.NotFoundException
import ltd.hlaeja.exception.TypeNameDuplicateException
import ltd.hlaeja.form.TypeForm
import ltd.hlaeja.service.DeviceRegistryService
import ltd.hlaeja.util.Pagination
import ltd.hlaeja.util.Pagination.Companion.DEFAULT_PAGE
import ltd.hlaeja.util.Pagination.Companion.DEFAULT_SIZE
import ltd.hlaeja.util.Pagination.Companion.MAX
import ltd.hlaeja.util.Pagination.Companion.MIN
import ltd.hlaeja.util.toTypeForm
import ltd.hlaeja.util.toTypeRequest
import ltd.hlaeja.util.validationErrors
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/types")
class TypeController(
    private val deviceRegistryService: DeviceRegistryService,
) {

    @GetMapping(
        "",
        "/page-{page}",
        "/page-{page}/show-{show}",
    )
    fun getTypes(
        @PathVariable(required = false) @Min(MIN) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(MIN) @Max(MAX) show: Int = DEFAULT_SIZE,
        model: Model,
    ) = deviceRegistryService.getTypes(page, show)
        .collectList()
        .doOnNext { items ->
            model.addAttribute("items", items)
            model.addAttribute("pagination", Pagination(page, show, items.size, DEFAULT_SIZE))
        }
        .then(Mono.just("type/list"))

    @GetMapping("/create")
    fun getCreateType(
        model: Model,
    ): Mono<String> = Mono.just("type/form")
        .doOnNext {
            model.addAttribute("typeForm", TypeForm())
        }

    @PostMapping("/create")
    fun postCreateType(
        @Validated @ModelAttribute("typeForm") typeForm: TypeForm,
        bindingResult: BindingResult,
        model: Model,
    ): Mono<String> = if (bindingResult.hasErrors()) {
        model.addAttribute("typeForm", typeForm)
        model.addAttribute("validationErrors", validationErrors(bindingResult))
        Mono.just("type/form")
    } else {
        Mono.just(typeForm)
            .flatMap { deviceRegistryService.createType(it.toTypeRequest()) }
            .map { "redirect:/types" }
            .onErrorResume { error ->
                val errorMessage = when (error) {
                    is TypeNameDuplicateException -> "Type name already exists. Please choose another."
                    else -> "An unexpected error occurred. Please try again later."
                }
                model.addAttribute("validationErrors", listOf(errorMessage))
                Mono.just("type/form")
            }
    }

    @GetMapping("/edit-{type}")
    fun getEditType(
        @PathVariable type: UUID,
        model: Model,
    ): Mono<String> = deviceRegistryService.getType(type)
        .doOnNext {
            model.addAttribute("type", it)
            model.addAttribute("typeForm", it.toTypeForm())
        }
        .then(Mono.just("type/form"))

    @PostMapping("/edit-{type}")
    fun postEditType(
        @PathVariable type: UUID,
        @Validated @ModelAttribute("typeForm") typeForm: TypeForm,
        bindingResult: BindingResult,
        model: Model,
    ): Mono<String> = if (bindingResult.hasErrors()) {
        deviceRegistryService.getType(type)
            .doOnNext {
                model.addAttribute("type", it)
                model.addAttribute("typeForm", typeForm)
                model.addAttribute("validationErrors", validationErrors(bindingResult))
            }
            .then(
                Mono.just("type/form"),
            )
    } else {
        Mono.just(typeForm)
            .flatMap { deviceRegistryService.updateType(type, it.toTypeRequest()) }
            .doOnNext {
                model.addAttribute("successMessage", listOf("Saved changes!!!"))
                model.addAttribute("type", it)
                model.addAttribute("typeForm", it.toTypeForm())
            }
            .then(Mono.just("type/form"))
            .onErrorResume { error ->
                val errorMessage = when (error) {
                    is NoChangeException -> Pair("successMessage", "No change to save.")
                    is NotFoundException -> Pair("validationErrors", "User dont exists. how did this happen?")
                    is TypeNameDuplicateException -> Pair(
                        "validationErrors",
                        "Type name already exists. Please choose another.",
                    )

                    else -> Pair("validationErrors", "An unexpected error occurred. Please try again later.")
                }
                deviceRegistryService.getType(type)
                    .doOnNext {
                        model.addAttribute(errorMessage.first, listOf(errorMessage.second))
                        model.addAttribute("type", it)
                        model.addAttribute("typeForm", typeForm)
                    }
                    .then(
                        Mono.just("type/form"),
                    )
            }
    }
}
