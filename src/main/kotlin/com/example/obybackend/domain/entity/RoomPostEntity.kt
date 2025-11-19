package com.example.obybackend.domain.entity

import com.example.obybackend.common.uuid.UuidGenerator
import com.example.obybackend.domain.value.RoomPostDescription
import com.example.obybackend.domain.value.RoomPostTitle
import java.time.LocalDateTime
import java.util.UUID

/**
 * 部屋投稿エンティティ
 *
 * @property id 部屋投稿ID (UUIDv7)
 * @property userId 投稿者のユーザーID
 * @property title 投稿のタイトル (1-100文字)
 * @property imageUrl 投稿画像のURL
 * @property description 投稿の説明文 (最大1000文字, nullable)
 * @property createdAt 作成日時
 * @property updatedAt 更新日時
 */
data class RoomPostEntity(
    val id: UUID = UuidGenerator.generate(),
    val userId: UUID,
    val title: RoomPostTitle,
    val imageUrl: String,
    val description: RoomPostDescription?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
