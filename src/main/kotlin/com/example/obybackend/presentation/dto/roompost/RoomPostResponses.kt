package com.example.obybackend.presentation.dto.roompost

import com.example.obybackend.usecase.roompost.CreateRoomPostOutput
import com.example.obybackend.usecase.roompost.GetRoomPostDetailOutput
import com.example.obybackend.usecase.roompost.GetRoomPostsOutput
import com.example.obybackend.usecase.roompost.RoomPostListItem
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 部屋投稿レスポンス（作成用）
 */
@Schema(description = "部屋投稿情報")
data class RoomPostResponse(
    @Schema(description = "部屋投稿ID", example = "018d1234-5678-7abc-def0-123456789abc")
    val id: String,
    @Schema(description = "投稿者のユーザーID", example = "018d1234-5678-7abc-def0-123456789def")
    val userId: String,
    @Schema(description = "投稿タイトル", example = "リビングルームの様子")
    val title: String,
    @Schema(description = "画像URL", example = "http://localhost:9000/oby/room-posts/xxx/yyy.jpg")
    val imageUrl: String,
    @Schema(description = "投稿の説明文", required = false, example = "掃除前の状態です")
    val description: String?,
    @Schema(description = "作成日時", example = "2024-01-15T10:30:00")
    val createdAt: String,
    @Schema(description = "更新日時", example = "2024-01-15T10:30:00")
    val updatedAt: String,
) {
    companion object {
        fun from(output: CreateRoomPostOutput): RoomPostResponse {
            return RoomPostResponse(
                id = output.id,
                userId = output.userId,
                title = output.title,
                imageUrl = output.imageUrl,
                description = output.description,
                createdAt = output.createdAt,
                updatedAt = output.updatedAt,
            )
        }
    }
}

/**
 * 部屋投稿詳細レスポンス（ユーザー情報付き）
 */
@Schema(description = "部屋投稿詳細情報（ユーザー情報含む）")
data class RoomPostDetailResponse(
    @Schema(description = "部屋投稿ID", example = "018d1234-5678-7abc-def0-123456789abc")
    val id: String,
    @Schema(description = "投稿者のユーザーID", example = "018d1234-5678-7abc-def0-123456789def")
    val userId: String,
    @Schema(description = "投稿者のニックネーム", example = "ユーザー太郎")
    val userNickname: String,
    @Schema(description = "投稿者のアイコン画像URL", required = false, example = "http://localhost:9000/oby/avatars/xxx.jpg")
    val userAvatarUrl: String?,
    @Schema(description = "投稿タイトル", example = "リビングルームの様子")
    val title: String,
    @Schema(description = "画像URL", example = "http://localhost:9000/oby/room-posts/xxx/yyy.jpg")
    val imageUrl: String,
    @Schema(description = "投稿の説明文", required = false, example = "掃除前の状態です")
    val description: String?,
    @Schema(description = "作成日時", example = "2024-01-15T10:30:00")
    val createdAt: String,
    @Schema(description = "更新日時", example = "2024-01-15T10:30:00")
    val updatedAt: String,
) {
    companion object {
        fun from(output: GetRoomPostDetailOutput): RoomPostDetailResponse {
            return RoomPostDetailResponse(
                id = output.id,
                userId = output.userId,
                userNickname = output.userNickname,
                userAvatarUrl = output.userAvatarUrl,
                title = output.title,
                imageUrl = output.imageUrl,
                description = output.description,
                createdAt = output.createdAt,
                updatedAt = output.updatedAt,
            )
        }
    }
}

/**
 * 部屋投稿一覧アイテムレスポンス
 */
@Schema(description = "部屋投稿一覧アイテム")
data class RoomPostListItemResponse(
    @Schema(description = "部屋投稿ID", example = "018d1234-5678-7abc-def0-123456789abc")
    val id: String,
    @Schema(description = "投稿者のユーザーID", example = "018d1234-5678-7abc-def0-123456789def")
    val userId: String,
    @Schema(description = "投稿者のニックネーム", example = "ユーザー太郎")
    val userNickname: String,
    @Schema(description = "投稿者のアイコン画像URL", required = false, example = "http://localhost:9000/oby/avatars/xxx.jpg")
    val userAvatarUrl: String?,
    @Schema(description = "投稿タイトル", example = "リビングルームの様子")
    val title: String,
    @Schema(description = "画像URL", example = "http://localhost:9000/oby/room-posts/xxx/yyy.jpg")
    val imageUrl: String,
    @Schema(description = "投稿の説明文", required = false, example = "掃除前の状態です")
    val description: String?,
    @Schema(description = "作成日時", example = "2024-01-15T10:30:00")
    val createdAt: String,
) {
    companion object {
        fun from(item: RoomPostListItem): RoomPostListItemResponse {
            return RoomPostListItemResponse(
                id = item.id,
                userId = item.userId,
                userNickname = item.userNickname,
                userAvatarUrl = item.userAvatarUrl,
                title = item.title,
                imageUrl = item.imageUrl,
                description = item.description,
                createdAt = item.createdAt,
            )
        }
    }
}

/**
 * 部屋投稿一覧レスポンス（カーソルページネーション）
 */
@Schema(description = "部屋投稿一覧レスポンス（カーソルベースページネーション）")
data class RoomPostListResponse(
    @Schema(description = "部屋投稿一覧")
    val items: List<RoomPostListItemResponse>,
    @Schema(
        description = "次のページのカーソル（最後のページの場合はnull）",
        required = false,
        example = "018d1234-5678-7abc-def0-123456789abc",
    )
    val nextCursor: String?,
    @Schema(description = "次のページが存在するか", example = "true")
    val hasMore: Boolean,
) {
    companion object {
        fun from(output: GetRoomPostsOutput): RoomPostListResponse {
            return RoomPostListResponse(
                items = output.items.map { RoomPostListItemResponse.from(it) },
                nextCursor = output.nextCursor,
                hasMore = output.hasMore,
            )
        }
    }
}
