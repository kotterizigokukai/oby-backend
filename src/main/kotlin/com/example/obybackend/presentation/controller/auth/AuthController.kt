package com.example.obybackend.presentation.controller.auth

import com.example.obybackend.presentation.dto.auth.UserResponse
import com.example.obybackend.presentation.util.OAuth2UserUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 認証関連API
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "認証管理API")
class AuthController(
    private val oauth2UserUtils: OAuth2UserUtils,
) {
    /**
     * 現在ログイン中のユーザー情報を取得
     */
    @GetMapping("/user")
    @Operation(operationId = "getCurrentUser", summary = "現在のユーザー情報取得")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
            ApiResponse(responseCode = "401", description = "認証が必要です", content = [Content()]),
            ApiResponse(responseCode = "404", description = "ユーザーが見つかりません", content = [Content()]),
        ],
    )
    fun getCurrentUser(
        @AuthenticationPrincipal oauth2User: OAuth2User,
    ): ResponseEntity<UserResponse> {
        val user = oauth2UserUtils.extractUser(oauth2User)

        return ResponseEntity.ok(
            UserResponse(
                userId = user.id,
                email = user.email,
            ),
        )
    }

    /**
     * ログアウト
     */
    @PostMapping("/logout")
    @Operation(operationId = "logout", summary = "ログアウト")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
        ],
    )
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            SecurityContextLogoutHandler().logout(request, response, auth)
        }
        return ResponseEntity.ok().build()
    }
}
