package ltd.hlaeja.form

import jakarta.validation.constraints.Size

data class TypeForm(
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    val name: String = "",
    @field:Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters")
    val description: String = "",
)
