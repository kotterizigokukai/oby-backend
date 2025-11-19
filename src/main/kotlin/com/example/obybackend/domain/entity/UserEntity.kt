package com.example.obybackend.domain.entity

import com.example.obybackend.common.uuid.UuidGenerator
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
    val id: UUID = UuidGenerator.generate(),
    val googleSub: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
