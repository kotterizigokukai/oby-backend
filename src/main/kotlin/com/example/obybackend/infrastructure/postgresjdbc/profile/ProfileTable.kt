package com.example.obybackend.infrastructure.postgresjdbc.profile

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

/**
 * profilesテーブルのJDBCモデル
 *
 * @property id プロフィールID (主キー)
 * @property userId ユーザーID (外部キー、一意制約)
 * @property nickname ニックネーム (1-50文字)
 * @property avatarUrl アイコン画像URL (nullable)
 * @property bio 自己紹介文 (最大500文字、nullable)
 * @property createdAt 作成日時 (UTC)
 * @property updatedAt 更新日時 (UTC)
 */
@Table("profiles")
data class ProfileTable(
    @Id
    val id: UUID,
    val userId: UUID,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
)
