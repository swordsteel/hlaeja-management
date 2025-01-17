package ltd.hlaeja.util

import org.springframework.security.core.Authentication as SpringAuthentication

import ltd.hlaeja.library.accountRegistry.Authentication

fun SpringAuthentication.toAuthenticationRequest(): Authentication.Request = Authentication.Request(
    principal as String,
    credentials as String,
)
