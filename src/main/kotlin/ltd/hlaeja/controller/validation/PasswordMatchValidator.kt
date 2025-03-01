package ltd.hlaeja.controller.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import ltd.hlaeja.form.AccountForm

class PasswordMatchValidator : ConstraintValidator<PasswordMatch, AccountForm> {
    override fun isValid(form: AccountForm, context: ConstraintValidatorContext): Boolean {
        val password = form.password?.toString()
        val passwordConfirm = form.passwordConfirm?.toString()
        return if (password.isNullOrEmpty() && passwordConfirm.isNullOrEmpty()) {
            true
        } else {
            password == passwordConfirm
        }
    }
}
