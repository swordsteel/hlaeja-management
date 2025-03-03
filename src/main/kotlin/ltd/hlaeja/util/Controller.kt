package ltd.hlaeja.util

import org.springframework.validation.BindingResult

fun validationErrors(
    bindingResult: BindingResult,
): List<String> = if (bindingResult.hasErrors()) {
    bindingResult.allErrors.map { error -> error.defaultMessage ?: "Unknown validation error" }
} else {
    emptyList()
}
