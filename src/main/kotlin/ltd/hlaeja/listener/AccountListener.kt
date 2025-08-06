package ltd.hlaeja.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import ltd.hlaeja.library.accountRegistry.event.AccountMessage
import ltd.hlaeja.service.RedisSessionService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

val log = KotlinLogging.logger {}

@Component
class AccountListener(
    private val sessionService: RedisSessionService,
) {

    @KafkaListener(topics = ["account"])
    fun handleRemoteAccountEvent(record: ConsumerRecord<String, AccountMessage>) {
        log.trace { "Received event: ${record.key()} for user: ${record.value().userId}" }
        if (record.key() == "change" && record.value().change.any { it in setOf("enabled", "username", "roles") }) {
            sessionService.updateUser(record.value().userId).subscribe()
        }
    }
}
