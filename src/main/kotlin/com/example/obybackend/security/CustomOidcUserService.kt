package com.example.obybackend.security

import com.example.obybackend.user.UserAccountService
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component

@Component
class CustomOidcUserService(
    private val userAccountService: UserAccountService,
) : OAuth2UserService<OidcUserRequest, OidcUser> {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val delegate = OidcUserService()

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val oidcUser = delegate.loadUser(userRequest)
        val subject = oidcUser.subject
        val email = oidcUser.email ?: error("Google account does not expose an email address.")
        val displayName = oidcUser.fullName ?: oidcUser.givenName ?: email.substringBefore("@")
        val avatarUrl = oidcUser.picture

        val account = userAccountService.registerGoogleUser(
            subject = subject,
            email = email,
            displayName = displayName,
            avatarUrl = avatarUrl,
        )

        logger.debug("Authenticated Google user {} mapped to internal id {}", subject, account.id)

        val authorities = setOf(SimpleGrantedAuthority("ROLE_${account.role.name}"))

        val userInfo = oidcUser.userInfo
        return if (userInfo != null) {
            DefaultOidcUser(authorities, oidcUser.idToken, userInfo, "sub")
        } else {
            DefaultOidcUser(authorities, oidcUser.idToken, "sub")
        }
    }
}
