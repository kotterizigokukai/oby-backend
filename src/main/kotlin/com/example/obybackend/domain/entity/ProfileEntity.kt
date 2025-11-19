package com.example.obybackend.domain.entity

import com.example.obybackend.common.timestamp.TimestampGenerator
import com.example.obybackend.common.uuid.UuidGenerator
import com.example.obybackend.domain.value.AvatarUrl
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import java.time.Instant
import java.util.UUID

/**
 * プロフィールエンティティ
 *
 * @property id プロフィールID
 * @property userId ユーザーID
 * @property nickname ニックネーム (1-50文字)
 * @property avatarUrl アイコン画像URL (nullable)
 * @property bio 自己紹介文 (最大500文字, nullable)
 * @property createdAt 作成日時 (UTC)
 * @property updatedAt 更新日時 (UTC)
 */
data class ProfileEntity(
    val id: UUID = UuidGenerator.generate(),
    val userId: UUID,
    val nickname: Nickname,
    val avatarUrl: AvatarUrl?,
    val bio: Bio?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        /**
         * 新規プロフィールエンティティを作成
         */
        fun create(
            userId: UUID,
            nickname: Nickname,
            avatarUrl: AvatarUrl?,
            bio: Bio?,
            timestampGenerator: TimestampGenerator,
        ): ProfileEntity {
            val now = timestampGenerator.now()
            return ProfileEntity(
                userId = userId,
                nickname = nickname,
                avatarUrl = avatarUrl,
                bio = bio,
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}
