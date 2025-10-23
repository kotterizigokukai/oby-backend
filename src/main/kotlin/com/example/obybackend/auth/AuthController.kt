package com.example.obybackend.auth

import com.example.obybackend.user.AuthProvider
import com.example.obybackend.user.UserAccountService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userAccountService: UserAccountService,
) {
    @GetMapping("/me")
    fun currentUser(
        @AuthenticationPrincipal principal: OidcUser,
    ): AuthenticatedUserResponse {
        val account = userAccountService.findBySubject(AuthProvider.GOOGLE, principal.subject)

        return AuthenticatedUserResponse(
            id = account?.id?.toString(),
            email = principal.email,
            name = principal.fullName ?: principal.givenName ?: principal.email,
            pictureUrl = principal.picture,
            roles = principal.authorities.map { it.authority },
        )
    }
}
