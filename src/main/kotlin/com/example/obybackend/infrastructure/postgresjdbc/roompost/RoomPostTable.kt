package com.example.obybackend.infrastructure.postgresjdbc.roompost

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

/**
 * room_postsテーブルのJDBCモデル
 *
 * @property id 部屋投稿ID (主キー、UUIDv7)
 * @property userId 投稿者のユーザーID (外部キー)
 * @property title 投稿のタイトル (1-100文字)
 * @property imageUrl 投稿画像のURL
 * @property description 投稿の説明文 (最大1000文字、nullable)
 * @property createdAt 作成日時
 * @property updatedAt 更新日時
 */
@Table("room_posts")
data class RoomPostTable(
    @Id
    val id: UUID,
    val userId: UUID,
    val title: String,
    val imageUrl: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
