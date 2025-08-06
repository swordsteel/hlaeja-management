package ltd.hlaeja.service

import java.util.UUID
import ltd.hlaeja.security.user.RemoteAuthentication
import ltd.hlaeja.util.RemoteAuthenticationUtil
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.session.data.redis.ReactiveRedisIndexedSessionRepository
import org.springframework.session.data.redis.ReactiveRedisIndexedSessionRepository.RedisSession
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RedisSessionService(
    private val redisSessionRepository: ReactiveRedisIndexedSessionRepository,
    private val accountRegistryService: AccountRegistryService,
) {

    fun deleteUser(user: UUID): Flux<RedisSession> = findByUser(user)
        .flatMapMany { sessions -> Flux.fromIterable(sessions.values) }
        .flatMap(::delete)

    fun updateUser(
        user: UUID,
    ): Flux<RedisSession> = findByUser(user)
        .flatMapMany { map ->
            getUserAccount(user).flatMapMany { response ->
                Flux.fromIterable(map.values).map { session -> session to response }
            }
        }
        .flatMap { (session: RedisSession, response: RemoteAuthentication) ->
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextImpl(response))
            save(session)
        }

    private fun getUserAccount(
        user: UUID,
    ) = accountRegistryService.getAccount(user)
        .map(RemoteAuthenticationUtil::fromAccountResponse)

    private fun findByUser(
        user: UUID,
    ): Mono<MutableMap<String, RedisSession>> = redisSessionRepository.findByPrincipalName(user.toString())
        .doOnNext { ltd.hlaeja.listener.log.trace { "Found ${it.size} session(s) for user $user" } }
        .filter { map -> map.isNotEmpty() }

    private fun save(
        session: RedisSession,
    ): Mono<RedisSession> = redisSessionRepository.save(session)
        .thenReturn(session)
        .doOnNext { ltd.hlaeja.listener.log.trace { "Save session: ${it.id}" } }

    private fun delete(
        session: RedisSession,
    ): Mono<RedisSession> = redisSessionRepository.deleteById(session.id)
        .thenReturn(session)
        .doOnNext { ltd.hlaeja.listener.log.trace { "Deleted session: ${it.id}" } }
}
