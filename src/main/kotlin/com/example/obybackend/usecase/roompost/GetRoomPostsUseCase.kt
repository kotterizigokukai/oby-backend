package com.example.obybackend.usecase.roompost

import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.RoomPostRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 部屋投稿一覧取得ユースケース（カーソルベースページネーション）
 */
@Service
class GetRoomPostsUseCase(
    private val roomPostRepository: RoomPostRepository,
    private val profileRepository: ProfileRepository,
) {
    private val logger = LoggerFactory.getLogger(GetRoomPostsUseCase::class.java)

    companion object {
        private const val DEFAULT_LIMIT = 10
        private const val MAX_LIMIT = 50
    }

    @Transactional(readOnly = true)
    fun execute(input: GetRoomPostsInput): GetRoomPostsOutput {
        // 1. limitを制限
        val safeLimit = minOf(input.limit ?: DEFAULT_LIMIT, MAX_LIMIT)

        // 2. limit+1件取得してhasMoreを判定
        val roomPosts = roomPostRepository.findAll(input.cursor, safeLimit + 1)

        // 3. hasMore判定
        val hasMore = roomPosts.size > safeLimit
        val items = if (hasMore) roomPosts.take(safeLimit) else roomPosts

        // 4. ユーザー情報を結合
        val roomPostsWithUser =
            items.map { roomPost ->
                val profile = profileRepository.findByUserId(roomPost.userId)
                RoomPostListItem(
                    id = roomPost.id.toString(),
                    userId = roomPost.userId.toString(),
                    userNickname = profile?.nickname?.value ?: "Unknown User",
                    userAvatarUrl = profile?.avatarUrl?.value,
                    title = roomPost.title.value,
                    imageUrl = roomPost.imageUrl,
                    description = roomPost.description?.value,
                    createdAt = roomPost.createdAt.toString(),
                )
            }

        // 5. nextCursorを設定
        val nextCursor = if (hasMore) items.lastOrNull()?.id?.toString() else null

        logger.info("Retrieved room posts: count=${roomPostsWithUser.size}, hasMore=$hasMore")

        return GetRoomPostsOutput(
            items = roomPostsWithUser,
            nextCursor = nextCursor,
            hasMore = hasMore,
        )
    }
}

/**
 * 部屋投稿一覧取得Input
 */
data class GetRoomPostsInput(
    val cursor: UUID?,
    val limit: Int?,
)

/**
 * 部屋投稿一覧取得Output
 */
data class GetRoomPostsOutput(
    val items: List<RoomPostListItem>,
    val nextCursor: String?,
    val hasMore: Boolean,
)

/**
 * 部屋投稿一覧アイテム
 */
data class RoomPostListItem(
    val id: String,
    val userId: String,
    val userNickname: String,
    val userAvatarUrl: String?,
    val title: String,
    val imageUrl: String,
    val description: String?,
    val createdAt: String,
)
