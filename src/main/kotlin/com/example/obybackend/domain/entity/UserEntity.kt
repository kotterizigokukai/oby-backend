package com.example.obybackend.domain.entity

import java.time.LocalDateTime
import java.util.UUID

/**
 * ユーザーエンティティ (Google OAuth認証)
 *
 * @property id ユーザーID (アプリ内主キー)
 * @property googleSub GoogleのユニークID (sub claim)
 * @property email メールアドレス
 * @property createdAt 作成日時
 * @property updatedAt 更新日時
 */
data class UserEntity(
    val id: UUID,
    val googleSub: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
