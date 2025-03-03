package ltd.hlaeja.util

import ltd.hlaeja.form.AccountForm
import ltd.hlaeja.form.TypeForm
import ltd.hlaeja.library.accountRegistry.Account
import ltd.hlaeja.library.accountRegistry.Authentication
import ltd.hlaeja.library.deviceRegistry.Type
import org.springframework.security.core.Authentication as SpringAuthentication

fun SpringAuthentication.toAuthenticationRequest(): Authentication.Request = Authentication.Request(
    principal as String,
    credentials as String,
)

fun AccountForm.toAccountRequest(): Account.Request = Account.Request(
    username = username,
    password = if (password.isNullOrEmpty()) null else password,
    enabled = enabled,
    roles = roles.map { "ROLE_${it.uppercase()}" },
)

fun Account.Response.toAccountForm(): AccountForm = AccountForm(
    username = username,
    enabled = enabled,
    roles = roles.map {
        it.removePrefix("ROLE_")
            .lowercase()
            .replaceFirstChar { char -> char.uppercase() }
    },
)

fun TypeForm.toTypeRequest(): Type.Request = Type.Request(
    name = name,
    description = description,
)

fun Type.Response.toTypeForm(): Type.Request = Type.Request(
    name = name,
    description = description,
)
