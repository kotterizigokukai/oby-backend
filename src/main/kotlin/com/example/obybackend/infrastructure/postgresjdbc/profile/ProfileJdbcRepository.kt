package com.example.obybackend.infrastructure.postgresjdbc.profile

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

/**
 * Profile用 Spring Data JDBC リポジトリ
 *
 * すべてのクエリを明示的に記述
 */
interface ProfileJdbcRepository : CrudRepository<ProfileTable, UUID> {
    /**
     * ユーザーIDでプロフィールを取得
     */
    @Query("SELECT * FROM profiles WHERE user_id = :userId")
    fun findByUserId(
        @Param("userId") userId: UUID,
    ): ProfileTable?

    /**
     * IDでプロフィールを取得
     */
    @Query("SELECT * FROM profiles WHERE id = :id")
    fun findByProfileId(
        @Param("id") id: UUID,
    ): ProfileTable?

    /**
     * プロフィールを保存または更新 (UPSERT)
     */
    @Modifying
    @Query(
        """
        INSERT INTO profiles (id, user_id, nickname, avatar_url, bio, created_at, updated_at)
        VALUES (:id, :userId, :nickname, :avatarUrl, :bio, :createdAt, :updatedAt)
        ON CONFLICT (id) DO UPDATE SET
            nickname = EXCLUDED.nickname,
            avatar_url = EXCLUDED.avatar_url,
            bio = EXCLUDED.bio,
            updated_at = EXCLUDED.updated_at
        """,
    )
    fun upsert(
        @Param("id") id: UUID,
        @Param("userId") userId: UUID,
        @Param("nickname") nickname: String,
        @Param("avatarUrl") avatarUrl: String?,
        @Param("bio") bio: String?,
        @Param("createdAt") createdAt: Instant,
        @Param("updatedAt") updatedAt: Instant,
    )

    /**
     * プロフィールを削除
     */
    @Modifying
    @Query("DELETE FROM profiles WHERE id = :id")
    fun deleteByProfileId(
        @Param("id") id: UUID,
    )
}
