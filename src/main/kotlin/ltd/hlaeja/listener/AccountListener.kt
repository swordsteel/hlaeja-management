package ltd.hlaeja.listener

import io.github.oshai.kotlinlogging.KotlinLogging
import ltd.hlaeja.library.accountRegistry.event.AccountMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

val log = KotlinLogging.logger {}

@Component
class AccountListener {

    @KafkaListener(topics = ["account"])
    fun handleRemoteAccountEvent(record: ConsumerRecord<String, AccountMessage>) {
        log.trace { "Received event: ${record.key()} for user: ${record.value().userId}" }
    }
}
