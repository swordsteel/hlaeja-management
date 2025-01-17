package ltd.hlaeja.security

import java.util.UUID

data class RemoteUserDetail(
    val id: UUID,
    val username: String,
)
