package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.exception.DeviceRegistryException
import ltd.hlaeja.exception.HlaejaException
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.library.deviceRegistry.Types
import ltd.hlaeja.property.DeviceRegistryProperty
import ltd.hlaeja.util.deviceRegistryType
import ltd.hlaeja.util.deviceRegistryTypes
import ltd.hlaeja.util.deviceRegistryTypesCreate
import ltd.hlaeja.util.deviceRegistryTypesUpdate
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DeviceRegistryService(
    private val webClient: WebClient,
    private val property: DeviceRegistryProperty,
) {

    fun getTypes(
        page: Int,
        show: Int,
    ): Flux<Types.Response> = webClient.deviceRegistryTypes(page, show, property)

    fun createType(
        request: Type.Request,
    ): Mono<Type.Response> = webClient.deviceRegistryTypesCreate(request, property)
        .onErrorResume { error ->
            when (error) {
                is DeviceRegistryException -> Mono.error(error)
                else -> Mono.error(ResponseStatusException(BAD_REQUEST, error.message))
            }
        }

    fun getType(
        type: UUID,
    ): Mono<Type.Response> = webClient.deviceRegistryType(type, property)
        .onErrorResume { error ->
            when (error) {
                is ResponseStatusException -> Mono.error(error)
                else -> Mono.error(ResponseStatusException(BAD_REQUEST, error.message))
            }
        }

    fun updateType(
        type: UUID,
        request: Type.Request,
    ): Mono<Type.Response> = webClient.deviceRegistryTypesUpdate(type, request, property)
        .onErrorResume(::errorHandler)

    private fun errorHandler(
        error: Throwable,
    ): Mono<out Type.Response> = when (error) {
        is HlaejaException -> Mono.error(error)
        else -> Mono.error(ResponseStatusException(BAD_REQUEST, error.message))
    }
}
