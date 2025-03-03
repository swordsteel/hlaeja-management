package ltd.hlaeja

import ltd.hlaeja.property.AccountRegistryProperty
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    AccountRegistryProperty::class,
    DeviceRegistryProperty::class,
)
@SpringBootApplication
class Application

fun main(vararg args: String) {
    runApplication<Application>(*args)
}
