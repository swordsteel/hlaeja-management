package ltd.hlaeja.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "device-registry")
data class DeviceRegistryProperty(
    val url: String,
)
