package ltd.hlaeja

import ltd.hlaeja.property.AccountRegistryProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(
    AccountRegistryProperty::class,
)
@SpringBootApplication
class Application

fun main(vararg args: String) {
    runApplication<Application>(*args)
}
