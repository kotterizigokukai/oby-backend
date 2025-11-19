package com.example.obybackend.presentation.dto.profile

import io.swagger.v3.oas.annotations.media.Schema

/**
 * プロフィール更新リクエスト
 */
@Schema(description = "プロフィール更新リクエスト")
data class UpdateProfileRequest(
    @Schema(description = "ニックネーム (1-50文字)", required = true, example = "ユーザー太郎")
    val nickname: String,
    @Schema(description = "自己紹介文 (最大500文字)", required = false, example = "こんにちは！")
    val bio: String?,
)
