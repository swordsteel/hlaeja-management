package ltd.hlaeja.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "account-registry")
data class AccountRegistryProperty(
    val url: String,
)
