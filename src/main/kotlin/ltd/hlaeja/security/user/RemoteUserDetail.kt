package ltd.hlaeja.security.user

import java.io.Serializable
import java.util.UUID

data class RemoteUserDetail(
    val id: UUID,
    val username: String,
) : Serializable {
    companion object {
        @Suppress("ConstPropertyName")
        private const val serialVersionUID = 1L
    }
}
