package com.example.obybackend.presentation.dto.profile

import com.example.obybackend.usecase.profile.DeleteAvatarOutput
import com.example.obybackend.usecase.profile.GetMyProfileOutput
import com.example.obybackend.usecase.profile.GetUserProfileOutput
import com.example.obybackend.usecase.profile.UpdateMyProfileOutput
import com.example.obybackend.usecase.profile.UploadAvatarOutput
import io.swagger.v3.oas.annotations.media.Schema

/**
 * プロフィールレスポンス
 */
@Schema(description = "プロフィール情報")
data class ProfileResponse(
    @Schema(description = "プロフィールID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: String,
    @Schema(description = "ユーザーID", example = "123e4567-e89b-12d3-a456-426614174001")
    val userId: String,
    @Schema(description = "ニックネーム", example = "ユーザー太郎")
    val nickname: String,
    @Schema(description = "アイコン画像URL", required = false, example = "http://localhost:9000/oby/avatars/xxx.jpg")
    val avatarUrl: String?,
    @Schema(description = "自己紹介文", required = false, example = "こんにちは！")
    val bio: String?,
) {
    companion object {
        fun from(output: GetMyProfileOutput): ProfileResponse {
            return ProfileResponse(
                id = output.id,
                userId = output.userId,
                nickname = output.nickname,
                avatarUrl = output.avatarUrl,
                bio = output.bio,
            )
        }

        fun from(output: GetUserProfileOutput): ProfileResponse {
            return ProfileResponse(
                id = output.id,
                userId = output.userId,
                nickname = output.nickname,
                avatarUrl = output.avatarUrl,
                bio = output.bio,
            )
        }

        fun from(output: UpdateMyProfileOutput): ProfileResponse {
            return ProfileResponse(
                id = output.id,
                userId = output.userId,
                nickname = output.nickname,
                avatarUrl = output.avatarUrl,
                bio = output.bio,
            )
        }

        fun from(output: UploadAvatarOutput): ProfileResponse {
            return ProfileResponse(
                id = output.id,
                userId = output.userId,
                nickname = output.nickname,
                avatarUrl = output.avatarUrl,
                bio = output.bio,
            )
        }

        fun from(output: DeleteAvatarOutput): ProfileResponse {
            return ProfileResponse(
                id = output.id,
                userId = output.userId,
                nickname = output.nickname,
                avatarUrl = output.avatarUrl,
                bio = output.bio,
            )
        }
    }
}
