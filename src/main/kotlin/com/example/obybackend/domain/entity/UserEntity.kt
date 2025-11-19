package com.example.obybackend.domain.entity

import com.example.obybackend.common.timestamp.TimestampGenerator
import com.example.obybackend.common.uuid.UuidGenerator
import java.time.Instant
import java.util.UUID

/**
 * ユーザーエンティティ (Google OAuth認証)
 *
 * @property id ユーザーID (アプリ内主キー)
 * @property googleSub GoogleのユニークID (sub claim)
 * @property email メールアドレス
 * @property createdAt 作成日時 (UTC)
 * @property updatedAt 更新日時 (UTC)
 */
data class UserEntity(
    val id: UUID = UuidGenerator.generate(),
    val googleSub: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        /**
         * 新規ユーザーエンティティを作成
         */
        fun create(
            googleSub: String,
            email: String,
            timestampGenerator: TimestampGenerator,
        ): UserEntity {
            val now = timestampGenerator.now()
            return UserEntity(
                googleSub = googleSub,
                email = email,
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}
