package com.example.obybackend.usecase.roompost

import com.example.obybackend.domain.exception.RoomPostNotFoundException
import com.example.obybackend.domain.repository.RoomPostRepository
import com.example.obybackend.infrastructure.objectstorage.RoomPostImageStorage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 部屋投稿削除ユースケース
 */
@Service
class DeleteRoomPostUseCase(
    private val roomPostRepository: RoomPostRepository,
    private val roomPostImageStorage: RoomPostImageStorage,
) {
    private val logger = LoggerFactory.getLogger(DeleteRoomPostUseCase::class.java)

    @Transactional
    fun execute(input: DeleteRoomPostInput) {
        // 1. 部屋投稿の所有者確認
        val isOwner = roomPostRepository.existsByIdAndUserId(input.roomPostId, input.requestUserId)
        if (!isOwner) {
            logger.warn(
                "User ${input.requestUserId} attempted to delete room post ${input.roomPostId} owned by another user",
            )
            throw RoomPostNotFoundException(input.roomPostId.toString())
        }

        // 2. 部屋投稿を取得して画像URLを取得
        val roomPost =
            roomPostRepository.findById(input.roomPostId)
                ?: throw RoomPostNotFoundException(input.roomPostId.toString())

        // 3. 画像を削除
        try {
            roomPostImageStorage.deleteRoomPostImage(roomPost.imageUrl)
            logger.info("Deleted room post image: url=${roomPost.imageUrl}")
        } catch (e: Exception) {
            // 画像削除失敗はログに記録するが処理は継続
            logger.warn("Failed to delete room post image: url=${roomPost.imageUrl}", e)
        }

        // 4. 部屋投稿を削除
        roomPostRepository.deleteById(input.roomPostId)

        logger.info("Deleted room post: id=${input.roomPostId}, userId=${input.requestUserId}")
    }
}

/**
 * 部屋投稿削除Input
 */
data class DeleteRoomPostInput(
    val roomPostId: UUID,
    val requestUserId: UUID,
)
