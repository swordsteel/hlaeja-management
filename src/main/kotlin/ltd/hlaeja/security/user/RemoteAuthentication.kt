package ltd.hlaeja.security.user

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

data class RemoteAuthentication(
    private val remoteUserDetail: RemoteUserDetail,
    private var authorities: MutableCollection<out GrantedAuthority>,
    private var authenticated: Boolean = false,
) : Authentication {

    override fun getName(): String = remoteUserDetail.id.toString()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getCredentials(): Any? = null

    override fun getDetails(): Any? = null

    override fun getPrincipal(): Any = remoteUserDetail

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    @Suppress("unused")
    fun hasRole(role: String): Boolean {
        authorities.forEach {
            if (it.authority.equals("role_$role", true)) {
                return true
            }
        }
        return false
    }
}
