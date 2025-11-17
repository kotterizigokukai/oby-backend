package com.example.obybackend.infrastructure.security

import com.example.obybackend.application.user.UserAccountApplicationService
import com.example.obybackend.domain.entity.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component

@Component
class CustomOidcUserService(
    private val userAccountService: UserAccountApplicationService,
) : OAuth2UserService<OidcUserRequest, OidcUser> {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val delegate = OidcUserService()

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = delegate.loadUser(userRequest)
        val subject = oidcUser.subject
        val email = oidcUser.email ?: error("Google account does not expose an email address.")

        val account =
            userAccountService.registerOrUpdateGoogleUser(
                subject = subject,
                email = email,
            )

        logger.debug("Authenticated Google user {} mapped to internal id {}", subject, account.id)

        // DBに保存している役割をSpring Securityの権限へマッピング
        val authorities = setOf(SimpleGrantedAuthority(account.role.asAuthority()))

        val userInfo = oidcUser.userInfo
        val delegate =
            if (userInfo != null) {
                DefaultOidcUser(authorities, oidcUser.idToken, userInfo, "sub")
            } else {
                DefaultOidcUser(authorities, oidcUser.idToken, "sub")
            }
        return ApplicationOidcUser(delegate = delegate, account = account)
    }
}

/**
 * DBのユーザーエンティティとSpring SecurityのOidcUserを橋渡しするカスタムプリンシパル。
 * Spring Security全体からアプリケーション固有のユーザー情報へアクセスできるようにする。
 */
class ApplicationOidcUser(
    private val delegate: OidcUser,
    val account: UserEntity,
) : OidcUser {
    override fun getClaims(): Map<String, Any> = delegate.claims

    override fun getUserInfo(): OidcUserInfo? = delegate.userInfo

    override fun getIdToken(): OidcIdToken = delegate.idToken

    override fun getAttributes(): Map<String, Any> = delegate.attributes

    override fun getAuthorities(): Collection<GrantedAuthority> = delegate.authorities

    override fun getName(): String = account.id.toString()
}
