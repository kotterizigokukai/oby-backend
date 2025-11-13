package com.example.obybackend.usecase.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.StorageService
import com.example.obybackend.domain.value.AvatarUrl
import com.example.obybackend.infrastructure.objectstorage.ImageProcessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * アバター画像アップロードユースケース
 */
@Service
class UploadAvatarUseCase(
    private val profileRepository: ProfileRepository,
    private val storageService: StorageService,
    private val imageProcessor: ImageProcessor,
) {
    @Transactional
    fun execute(input: UploadAvatarInput): UploadAvatarOutput {
        // 1. 既存プロフィール取得
        val profile =
            profileRepository.findByUserId(input.userId)
                ?: throw ProfileNotFoundException(input.userId.toString())

        // 2. 古い画像を削除 (存在する場合)
        profile.avatarUrl?.let { oldUrl ->
            try {
                val oldKey = oldUrl.extractStorageKey()
                storageService.delete(oldKey)
            } catch (e: Exception) {
                // 古い画像削除失敗は無視 (既に削除済みの可能性)
                // ログ記録などの処理を追加可能
            }
        }

        // 3. 画像処理 (リサイズ + JPEG変換)
        val processedImage = imageProcessor.processImage(input.imageData)

        // 4. 新しい画像をアップロード
        val key = "avatars/${input.userId}-${UUID.randomUUID()}.jpg"
        val newUrl = storageService.upload(key, processedImage, "image/jpeg")

        // 5. DBを更新
        val updatedProfile =
            profileRepository.updateAvatarUrl(
                userId = input.userId,
                avatarUrl = AvatarUrl(newUrl),
            )

        return UploadAvatarOutput.from(updatedProfile)
    }
}

/**
 * アバター画像アップロードInput
 */
data class UploadAvatarInput(
    val userId: UUID,
    val imageData: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadAvatarInput

        if (userId != other.userId) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}

/**
 * アバター画像アップロードOutput
 */
data class UploadAvatarOutput(
    val id: String,
    val userId: String,
    val nickname: String,
    val avatarUrl: String,
    val bio: String?,
) {
    companion object {
        fun from(profile: ProfileEntity): UploadAvatarOutput {
            return UploadAvatarOutput(
                id = profile.id.toString(),
                userId = profile.userId.toString(),
                nickname = profile.nickname.value,
                avatarUrl =
                    profile.avatarUrl?.value
                        ?: throw IllegalStateException("Avatar URL must not be null after upload"),
                bio = profile.bio?.value,
            )
        }
    }
}
