package com.example.obybackend.presentation.dto.auth

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * 認証ユーザー情報のレスポンス
 *
 * @property userId ユーザーID
 * @property email メールアドレス
 */
@Schema(description = "認証ユーザー情報")
data class UserResponse(
    @Schema(description = "ユーザーID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
    val userId: UUID,
    @Schema(description = "メールアドレス", required = true, example = "user@example.com")
    val email: String,
)
