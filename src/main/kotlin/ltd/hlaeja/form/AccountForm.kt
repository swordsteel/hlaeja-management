package ltd.hlaeja.form

data class AccountForm(
    val username: String,
    val role: String,
    val enabled: Boolean = false,
    val password: CharSequence? = null,
    val passwordConfirm: CharSequence? = null,
)
