package ltd.hlaeja.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import java.util.UUID
import ltd.hlaeja.library.accountRegistry.Account
import ltd.hlaeja.security.user.RemoteAuthentication
import ltd.hlaeja.security.user.RemoteUserDetail
import org.springframework.security.core.authority.SimpleGrantedAuthority

object RemoteAuthenticationUtil {

    fun fromJwtClaims(
        claims: Jws<Claims>,
    ): RemoteAuthentication = makeRemoteAuthentication(
        id = UUID.fromString(claims.payload["id"] as String),
        username = claims.payload["username"] as String,
        roles = (claims.payload["role"] as String).split(","),
        authenticated = true,
    )

    fun fromAccountResponse(
        response: Account.Response,
    ): RemoteAuthentication = makeRemoteAuthentication(
        id = response.id,
        username = response.username,
        roles = response.roles,
        authenticated = response.enabled,
    )

    private fun makeRemoteAuthentication(
        id: UUID,
        username: String,
        roles: List<String>,
        authenticated: Boolean,
    ) = RemoteAuthentication(
        remoteUserDetail = RemoteUserDetail(
            id = id,
            username = username,
        ),
        authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList(),
        authenticated = authenticated,
    )
}
