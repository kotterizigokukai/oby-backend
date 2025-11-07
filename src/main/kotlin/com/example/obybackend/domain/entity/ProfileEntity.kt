package com.example.obybackend.domain.entity

import com.example.obybackend.domain.value.AvatarUrl
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import java.time.LocalDateTime
import java.util.UUID

/**
 * プロフィールエンティティ
 *
 * @property id プロフィールID
 * @property userId ユーザーID
 * @property nickname ニックネーム (1-50文字)
 * @property avatarUrl アイコン画像URL (nullable)
 * @property bio 自己紹介文 (最大500文字, nullable)
 * @property createdAt 作成日時
 * @property updatedAt 更新日時
 */
data class ProfileEntity(
    val id: UUID,
    val userId: UUID,
    val nickname: Nickname,
    val avatarUrl: AvatarUrl?,
    val bio: Bio?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
