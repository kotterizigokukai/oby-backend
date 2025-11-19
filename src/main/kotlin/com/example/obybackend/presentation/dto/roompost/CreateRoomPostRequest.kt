package com.example.obybackend.presentation.dto.roompost

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 部屋投稿作成リクエスト
 *
 * 注: MultipartFileはSwagger仕様上表現できないため、imageはString型で定義していますが、
 * 実際のAPIではMultipartFileとして扱われます。
 */
@Schema(description = "部屋投稿作成リクエスト")
data class CreateRoomPostRequest(
    @Schema(description = "部屋の画像ファイル", required = true, type = "string", format = "binary")
    val image: String,
    @Schema(description = "投稿タイトル (1-100文字)", required = true, example = "リビングルームの様子")
    val title: String,
    @Schema(description = "投稿の説明文 (最大1000文字)", required = false, example = "掃除前の状態です")
    val description: String?,
)
