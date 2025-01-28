package ltd.hlaeja.util

import ltd.hlaeja.form.AccountForm
import ltd.hlaeja.library.accountRegistry.Account
import ltd.hlaeja.library.accountRegistry.Authentication
import org.springframework.security.core.Authentication as SpringAuthentication

fun SpringAuthentication.toAuthenticationRequest(): Authentication.Request = Authentication.Request(
    principal as String,
    credentials as String,
)

fun AccountForm.toAccountRequest(operation: (CharSequence?) -> CharSequence?): Account.Request = Account.Request(
    username = username,
    password = operation(password),
    enabled = enabled,
    roles = listOf("ROLE_${role.uppercase()}"),
)

fun Account.Response.toAccountForm(): AccountForm = AccountForm(
    username = username,
    enabled = enabled,
    role = roles.first().removePrefix("ROLE_").lowercase(),
)
