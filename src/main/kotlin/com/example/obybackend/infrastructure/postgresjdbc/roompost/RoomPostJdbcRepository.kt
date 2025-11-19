package com.example.obybackend.infrastructure.postgresjdbc.roompost

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

/**
 * RoomPost用 Spring Data JDBC リポジトリ
 *
 * すべてのクエリを明示的に記述
 */
interface RoomPostJdbcRepository : CrudRepository<RoomPostTable, UUID> {
    /**
     * IDで部屋投稿を取得
     */
    @Query("SELECT * FROM room_posts WHERE id = :id")
    fun findByRoomPostId(
        @Param("id") id: UUID,
    ): RoomPostTable?

    /**
     * 部屋投稿一覧を取得（カーソルベースページネーション）
     * cursor = null の場合は最新から取得
     * cursor が指定されている場合は、そのIDより小さいIDを取得（UUIDv7は時系列順）
     */
    @Query(
        """
        SELECT * FROM room_posts
        WHERE (:cursor::uuid IS NULL OR id < :cursor::uuid)
        ORDER BY id DESC
        LIMIT :limit
        """,
    )
    fun findAllWithCursor(
        @Param("cursor") cursor: UUID?,
        @Param("limit") limit: Int,
    ): List<RoomPostTable>

    /**
     * 部屋投稿を保存
     */
    @Modifying
    @Query(
        """
        INSERT INTO room_posts (id, user_id, title, image_url, description, created_at, updated_at)
        VALUES (:id, :userId, :title, :imageUrl, :description, :createdAt, :updatedAt)
        """,
    )
    fun insert(
        @Param("id") id: UUID,
        @Param("userId") userId: UUID,
        @Param("title") title: String,
        @Param("imageUrl") imageUrl: String,
        @Param("description") description: String?,
        @Param("createdAt") createdAt: Instant,
        @Param("updatedAt") updatedAt: Instant,
    )

    /**
     * 部屋投稿を削除
     */
    @Modifying
    @Query("DELETE FROM room_posts WHERE id = :id")
    fun deleteByRoomPostId(
        @Param("id") id: UUID,
    )

    /**
     * 指定されたIDとユーザーIDの部屋投稿が存在するかチェック
     */
    @Query("SELECT EXISTS(SELECT 1 FROM room_posts WHERE id = :id AND user_id = :userId)")
    fun existsByIdAndUserId(
        @Param("id") id: UUID,
        @Param("userId") userId: UUID,
    ): Boolean
}
