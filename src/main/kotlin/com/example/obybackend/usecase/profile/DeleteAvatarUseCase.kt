package com.example.obybackend.usecase.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.StorageService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * アバター画像削除ユースケース
 */
@Service
class DeleteAvatarUseCase(
    private val profileRepository: ProfileRepository,
    private val storageService: StorageService,
) {
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
            } catch (e: Exception) {
                // 削除失敗は無視 (既に削除済みの可能性)
                // ログ記録などの処理を追加可能
            }
        }

        // 3. DBからURLを削除
        val updatedProfile = profileRepository.deleteAvatarUrl(userId)

        return DeleteAvatarOutput.from(updatedProfile)
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
