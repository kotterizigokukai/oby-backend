package com.example.obybackend.infrastructure.postgresjdbc.user

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.UUID

/**
 * User用 Spring Data JDBC リポジトリ
 *
 * すべてのクエリを明示的に記述
 */
interface UserJdbcRepository : CrudRepository<UserTable, UUID> {
    /**
     * IDでユーザーを取得
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun findByUserId(
        @Param("id") id: UUID,
    ): UserTable?

    /**
     * Google Subでユーザーを取得
     */
    @Query("SELECT * FROM users WHERE google_sub = :googleSub")
    fun findByGoogleSub(
        @Param("googleSub") googleSub: String,
    ): UserTable?

    /**
     * メールアドレスでユーザーを取得
     */
    @Query("SELECT * FROM users WHERE email = :email")
    fun findByEmail(
        @Param("email") email: String,
    ): UserTable?

    /**
     * ユーザーを保存または更新 (UPSERT)
     */
    @Modifying
    @Query(
        """
        INSERT INTO users (id, google_sub, email, created_at, updated_at)
        VALUES (:id, :googleSub, :email, :createdAt, :updatedAt)
        ON CONFLICT (id) DO UPDATE SET
            email = EXCLUDED.email,
            updated_at = EXCLUDED.updated_at
        """,
    )
    fun upsert(
        @Param("id") id: UUID,
        @Param("googleSub") googleSub: String,
        @Param("email") email: String,
        @Param("createdAt") createdAt: LocalDateTime,
        @Param("updatedAt") updatedAt: LocalDateTime,
    )
}
