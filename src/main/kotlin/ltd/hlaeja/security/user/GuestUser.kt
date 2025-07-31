package ltd.hlaeja.security.user

import java.util.UUID

interface GuestUser {

    fun guestUser(): RemoteAuthentication = RemoteAuthentication(
        RemoteUserDetail(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "n/a",
        ),
        mutableListOf(),
    )
}
