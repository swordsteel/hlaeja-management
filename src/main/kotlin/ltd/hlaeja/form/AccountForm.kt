package ltd.hlaeja.form

data class AccountForm(
    val username: String,
    val password: CharSequence,
    val passwordConfirm: CharSequence,
    val role: String,
    val enabled: Boolean = false,
)
