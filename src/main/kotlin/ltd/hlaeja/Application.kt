package ltd.hlaeja

import ltd.hlaeja.property.AccountRegistryProperty
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisIndexedWebSession

@EnableConfigurationProperties(
    AccountRegistryProperty::class,
    DeviceRegistryProperty::class,
)
@EnableRedisIndexedWebSession
@SpringBootApplication
class Application

fun main(vararg args: String) {
    runApplication<Application>(*args)
}
