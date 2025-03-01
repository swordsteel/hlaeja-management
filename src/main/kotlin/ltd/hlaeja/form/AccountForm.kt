package ltd.hlaeja.form

import jakarta.validation.constraints.NotEmpty
import ltd.hlaeja.controller.validation.CreateGroup
import ltd.hlaeja.controller.validation.EditGroup
import ltd.hlaeja.controller.validation.PasswordMatch

@PasswordMatch(groups = [CreateGroup::class, EditGroup::class])
data class AccountForm(
    @field:NotEmpty(message = "Username cannot be empty", groups = [CreateGroup::class, EditGroup::class])
    var username: String,
    @field:NotEmpty(message = "At least one role must be selected", groups = [CreateGroup::class, EditGroup::class])
    var roles: List<String> = emptyList(),
    var enabled: Boolean = false,
    @field:NotEmpty(message = "Password cannot be empty", groups = [CreateGroup::class])
    var password: CharSequence? = null,
    @field:NotEmpty(message = "Password confirmation cannot be empty", groups = [CreateGroup::class])
    var passwordConfirm: CharSequence? = null,
)
