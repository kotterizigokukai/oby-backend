package com.example.obybackend.presentation.auth

import com.example.obybackend.infrastructure.security.ApplicationOidcUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @GetMapping("/me")
    fun currentUser(
        @AuthenticationPrincipal principal: ApplicationOidcUser,
    ): AuthenticatedUserResponse {
        return AuthenticatedUserResponse(
            id = principal.account.id.toString(),
            email = principal.account.email,
            name =
                principal.attributes["name"] as? String
                    ?: principal.attributes["given_name"] as? String
                    ?: principal.account.email,
            pictureUrl = principal.attributes["picture"] as? String,
            roles = principal.authorities.map { it.authority },
        )
    }
}
