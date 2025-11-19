package com.example.obybackend.usecase.profile

import com.example.obybackend.common.timestamp.TimestampGenerator
import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.StorageService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * アバター画像削除ユースケース
 *
 * ビジネスロジック（タイムスタンプ更新）を担当
 */
@Service
class DeleteAvatarUseCase(
    private val profileRepository: ProfileRepository,
    private val storageService: StorageService,
    private val timestampGenerator: TimestampGenerator,
) {
    private val logger = LoggerFactory.getLogger(DeleteAvatarUseCase::class.java)

    @Transactional
    fun execute(userId: UUID): DeleteAvatarOutput {
        // 1. 既存プロフィール取得
        val profile =
            profileRepository.findByUserId(userId)
                ?: throw ProfileNotFoundException(userId.toString())

        // 2. ストレージから画像を削除 (存在する場合)
        profile.avatarUrl?.let { oldUrl ->
            try {
                val key = oldUrl.extractStorageKey()
                storageService.delete(key)
                logger.info("Successfully deleted avatar from storage: key=$key, userId=$userId")
            } catch (e: Exception) {
                // 削除失敗は無視 (既に削除済みの可能性があるため処理は継続)
                logger.warn("Failed to delete avatar from storage: url=${oldUrl.value}, userId=$userId", e)
            }
        }

        // 3. DBを更新（avatarUrlをnullに設定し、updatedAtを現在時刻に設定）
        val updatedProfile =
            profile.copy(
                avatarUrl = null,
                updatedAt = timestampGenerator.now(),
            )

        val savedProfile = profileRepository.save(updatedProfile)

        return DeleteAvatarOutput.from(savedProfile)
    }
}

/**
 * アバター画像削除Output
 */
data class DeleteAvatarOutput(
    val id: String,
    val userId: String,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
) {
    companion object {
        fun from(profile: ProfileEntity): DeleteAvatarOutput {
            return DeleteAvatarOutput(
                id = profile.id.toString(),
                userId = profile.userId.toString(),
                nickname = profile.nickname.value,
                avatarUrl = profile.avatarUrl?.value,
                bio = profile.bio?.value,
            )
        }
    }
}
