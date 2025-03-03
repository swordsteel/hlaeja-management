package ltd.hlaeja.util

import java.util.UUID
import ltd.hlaeja.exception.DeviceRegistryException
import ltd.hlaeja.exception.NoChangeException
import ltd.hlaeja.exception.NotFoundException
import ltd.hlaeja.exception.TypeNameDuplicateException
import ltd.hlaeja.library.deviceRegistry.Type
import ltd.hlaeja.library.deviceRegistry.Types
import ltd.hlaeja.property.DeviceRegistryProperty
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun WebClient.deviceRegistryTypes(
    page: Int,
    size: Int,
    property: DeviceRegistryProperty,
): Flux<Types.Response> = get()
    .uri("${property.url}/types/page-$page/show-$size".also(::logCall))
    .retrieve()
    .bodyToFlux(Types.Response::class.java)

fun WebClient.deviceRegistryTypesCreate(
    request: Type.Request,
    property: DeviceRegistryProperty,
): Mono<Type.Response> = post()
    .uri("${property.url}/type".also(::logCall))
    .bodyValue(request)
    .retrieve()
    .onStatus(CONFLICT::equals) { throw TypeNameDuplicateException("Remote service returned 409") }
    .onStatus(BAD_REQUEST::equals) { throw DeviceRegistryException("Remote service returned 400") }
    .bodyToMono(Type.Response::class.java)

fun WebClient.deviceRegistryType(
    type: UUID,
    property: DeviceRegistryProperty,
): Mono<Type.Response> = get()
    .uri("${property.url}/type-$type".also(::logCall))
    .retrieve()
    .onStatus(NOT_FOUND::equals) { throw ResponseStatusException(NOT_FOUND) }
    .bodyToMono(Type.Response::class.java)

fun WebClient.deviceRegistryTypesUpdate(
    type: UUID,
    request: Type.Request,
    property: DeviceRegistryProperty,
): Mono<Type.Response> = put()
    .uri("${property.url}/type-$type".also(::logCall))
    .bodyValue(request)
    .retrieve()
    .onStatus(ACCEPTED::equals) { throw NoChangeException("Remote service returned 202") }
    .onStatus(BAD_REQUEST::equals) { throw DeviceRegistryException("Remote service returned 400") }
    .onStatus(NOT_FOUND::equals) { throw NotFoundException("Remote service returned 404") }
    .onStatus(CONFLICT::equals) { throw TypeNameDuplicateException("Remote service returned 409") }
    .bodyToMono(Type.Response::class.java)
