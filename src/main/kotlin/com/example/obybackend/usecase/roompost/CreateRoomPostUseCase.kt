package com.example.obybackend.usecase.roompost

import com.example.obybackend.common.uuid.UuidGenerator
import com.example.obybackend.domain.entity.RoomPostEntity
import com.example.obybackend.domain.repository.RoomPostRepository
import com.example.obybackend.domain.value.RoomPostDescription
import com.example.obybackend.domain.value.RoomPostTitle
import com.example.obybackend.infrastructure.objectstorage.RoomPostImageStorage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 部屋投稿作成ユースケース
 */
@Service
class CreateRoomPostUseCase(
    private val roomPostRepository: RoomPostRepository,
    private val roomPostImageStorage: RoomPostImageStorage,
) {
    private val logger = LoggerFactory.getLogger(CreateRoomPostUseCase::class.java)

    @Transactional
    fun execute(input: CreateRoomPostInput): CreateRoomPostOutput {
        // 1. UUIDv7を生成
        val roomPostId = UuidGenerator.generate()

        // 2. 画像をストレージにアップロード
        val imageUrl = roomPostImageStorage.uploadRoomPostImage(input.userId, input.imageData)

        logger.info("Uploaded room post image: imageUrl=$imageUrl, userId=${input.userId}")

        // 3. RoomPostEntityを作成
        val roomPost =
            RoomPostEntity(
                id = roomPostId,
                userId = input.userId,
                title = RoomPostTitle(input.title),
                imageUrl = imageUrl,
                description = input.description?.let { RoomPostDescription(it) },
            )

        // 4. リポジトリに保存
        val savedRoomPost = roomPostRepository.save(roomPost)

        logger.info("Created room post: id=${savedRoomPost.id}, userId=${input.userId}")

        // 5. 出力に変換
        return CreateRoomPostOutput.from(savedRoomPost)
    }
}

/**
 * 部屋投稿作成Input
 */
data class CreateRoomPostInput(
    val userId: UUID,
    val imageData: ByteArray,
    val title: String,
    val description: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreateRoomPostInput

        if (userId != other.userId) return false
        if (!imageData.contentEquals(other.imageData)) return false
        if (title != other.title) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + imageData.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }
}

/**
 * 部屋投稿作成Output
 */
data class CreateRoomPostOutput(
    val id: String,
    val userId: String,
    val title: String,
    val imageUrl: String,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun from(entity: RoomPostEntity): CreateRoomPostOutput {
            return CreateRoomPostOutput(
                id = entity.id.toString(),
                userId = entity.userId.toString(),
                title = entity.title.value,
                imageUrl = entity.imageUrl,
                description = entity.description?.value,
                createdAt = entity.createdAt.toString(),
                updatedAt = entity.updatedAt.toString(),
            )
        }
    }
}
