package ltd.hlaeja.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.util.UUID
import ltd.hlaeja.dto.Pagination
import ltd.hlaeja.exception.NoChangeException
import ltd.hlaeja.exception.NotFoundException
import ltd.hlaeja.exception.TypeNameDuplicateException
import ltd.hlaeja.form.TypeForm
import ltd.hlaeja.service.DeviceRegistryService
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
import reactor.core.publisher.Mono

@Controller
class TypeController(
    private val deviceRegistryService: DeviceRegistryService,
) {
    companion object {
        const val DEFAULT_PAGE: Int = 1
        const val DEFAULT_SIZE: Int = 25
        const val MIN: Long = 1
        const val MAX: Long = 100
    }

    @GetMapping(
        "/type",
        "/type/page-{page}",
        "/type/page-{page}/show-{show}",
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
        .then(Mono.just("device/type/list"))

    @GetMapping("/type/create")
    fun getCreateType(
        model: Model,
    ): Mono<String> = Mono.just("device/type/form")
        .doOnNext {
            model.addAttribute("typeForm", TypeForm())
        }

    @PostMapping("/type/create")
    fun postCreateType(
        @Validated @ModelAttribute("typeForm") typeForm: TypeForm,
        bindingResult: BindingResult,
        model: Model,
    ): Mono<String> = if (bindingResult.hasErrors()) {
        model.addAttribute("typeForm", typeForm)
        model.addAttribute("validationErrors", validationErrors(bindingResult))
        Mono.just("device/type/form")
    } else {
        Mono.just(typeForm)
            .flatMap { deviceRegistryService.createType(it.toTypeRequest()) }
            .map { "redirect:/type" }
            .onErrorResume { error ->
                val errorMessage = when (error) {
                    is TypeNameDuplicateException -> "Type name already exists. Please choose another."
                    else -> "An unexpected error occurred. Please try again later."
                }
                model.addAttribute("validationErrors", listOf(errorMessage))
                Mono.just("device/type/form")
            }
    }

    @GetMapping("/type-{type}")
    fun getEditType(
        @PathVariable type: UUID,
        model: Model,
    ): Mono<String> = deviceRegistryService.getType(type)
        .doOnNext {
            model.addAttribute("type", it)
            model.addAttribute("typeForm", it.toTypeForm())
        }
        .then(Mono.just("device/type/form"))

    @PostMapping("/type-{type}")
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
                Mono.just("device/type/form"),
            )
    } else {
        Mono.just(typeForm)
            .flatMap { deviceRegistryService.updateType(type, it.toTypeRequest()) }
            .doOnNext {
                model.addAttribute("successMessage", listOf("Saved changes!!!"))
                model.addAttribute("type", it)
                model.addAttribute("typeForm", it.toTypeForm())
            }
            .then(Mono.just("device/type/form"))
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
                        Mono.just("device/type/form"),
                    )
            }
    }
}
