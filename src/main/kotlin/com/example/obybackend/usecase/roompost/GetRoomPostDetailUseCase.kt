package com.example.obybackend.usecase.roompost

import com.example.obybackend.domain.exception.RoomPostNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.RoomPostRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 部屋投稿詳細取得ユースケース
 */
@Service
class GetRoomPostDetailUseCase(
    private val roomPostRepository: RoomPostRepository,
    private val profileRepository: ProfileRepository,
) {
    private val logger = LoggerFactory.getLogger(GetRoomPostDetailUseCase::class.java)

    @Transactional(readOnly = true)
    fun execute(input: GetRoomPostDetailInput): GetRoomPostDetailOutput {
        // 1. 部屋投稿取得
        val roomPost =
            roomPostRepository.findById(input.roomPostId)
                ?: throw RoomPostNotFoundException(input.roomPostId.toString())

        // 2. ユーザー情報を結合
        val profile = profileRepository.findByUserId(roomPost.userId)

        logger.info("Retrieved room post detail: id=${roomPost.id}, userId=${roomPost.userId}")

        return GetRoomPostDetailOutput(
            id = roomPost.id.toString(),
            userId = roomPost.userId.toString(),
            userNickname = profile?.nickname?.value ?: "Unknown User",
            userAvatarUrl = profile?.avatarUrl?.value,
            title = roomPost.title.value,
            imageUrl = roomPost.imageUrl,
            description = roomPost.description?.value,
            createdAt = roomPost.createdAt.toString(),
            updatedAt = roomPost.updatedAt.toString(),
        )
    }
}

/**
 * 部屋投稿詳細取得Input
 */
data class GetRoomPostDetailInput(
    val roomPostId: UUID,
)

/**
 * 部屋投稿詳細取得Output
 */
data class GetRoomPostDetailOutput(
    val id: String,
    val userId: String,
    val userNickname: String,
    val userAvatarUrl: String?,
    val title: String,
    val imageUrl: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
)
