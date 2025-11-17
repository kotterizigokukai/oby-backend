package com.example.obybackend.infrastructure.postgresjdbc.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * usersテーブルのJDBCモデル
 *
 * @property id ユーザーID (主キー)
 * @property googleSub GoogleのユニークID (sub claim)
 * @property email メールアドレス
 * @property createdAt 作成日時
 * @property updatedAt 更新日時
 */
@Table("users")
data class UserTable(
    @Id
    val id: UUID?,
    val googleSub: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
