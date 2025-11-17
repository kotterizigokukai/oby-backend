package com.example.obybackend.presentation.auth

import com.example.obybackend.infrastructure.security.ApplicationOidcUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class AuthController {
    @GetMapping("/me")
    fun currentUser(
        @AuthenticationPrincipal principal: ApplicationOidcUser?,
    ): AuthenticatedUserResponse {
        val authenticatedUser =
            principal
                ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated")

        return AuthenticatedUserResponse(
            id = authenticatedUser.account.id.toString(),
            email = authenticatedUser.account.email,
            name =
                authenticatedUser.attributes["name"] as? String
                    ?: authenticatedUser.attributes["given_name"] as? String
                    ?: authenticatedUser.account.email,
            pictureUrl = authenticatedUser.attributes["picture"] as? String,
            roles = authenticatedUser.authorities.map { it.authority },
        )
    }
}
