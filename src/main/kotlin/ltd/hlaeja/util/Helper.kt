package ltd.hlaeja.util

import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

fun logCall(url: String) = log.debug { "calling: $url" }
