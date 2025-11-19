package com.example.obybackend.infrastructure.postgresjdbc.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

/**
 * usersテーブルのJDBCモデル
 *
 * @property id ユーザーID (主キー)
 * @property googleSub GoogleのユニークID (sub claim)
 * @property email メールアドレス
 * @property createdAt 作成日時 (UTC)
 * @property updatedAt 更新日時 (UTC)
 */
@Table("users")
data class UserTable(
    @Id
    val id: UUID,
    val googleSub: String,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
