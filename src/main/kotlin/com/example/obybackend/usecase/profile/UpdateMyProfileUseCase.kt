package com.example.obybackend.usecase.profile

import com.example.obybackend.common.timestamp.TimestampGenerator
import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * プロフィール更新ユースケース
 *
 * ビジネスロジック（タイムスタンプ更新）を担当
 */
@Service
class UpdateMyProfileUseCase(
    private val profileRepository: ProfileRepository,
    private val timestampGenerator: TimestampGenerator,
) {
    @Transactional
    fun execute(input: UpdateMyProfileInput): UpdateMyProfileOutput {
        // 既存プロフィールを取得
        val existingProfile =
            profileRepository.findByUserId(input.userId)
                ?: throw ProfileNotFoundException(input.userId.toString())

        // 値オブジェクトに変換
        val nickname = Nickname(input.nickname)
        val bio = input.bio?.let { Bio(it) }

        // 更新されたエンティティを作成（updatedAtを現在時刻に設定）
        val updatedProfile =
            existingProfile.copy(
                nickname = nickname,
                bio = bio,
                updatedAt = timestampGenerator.now(),
            )

        // 保存
        val savedProfile = profileRepository.save(updatedProfile)

        return UpdateMyProfileOutput.from(savedProfile)
    }
}

/**
 * プロフィール更新Input
 */
data class UpdateMyProfileInput(
    val userId: UUID,
    val nickname: String,
    val bio: String?,
)

/**
 * プロフィール更新Output
 */
data class UpdateMyProfileOutput(
    val id: String,
    val userId: String,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
) {
    companion object {
        fun from(profile: ProfileEntity): UpdateMyProfileOutput {
            return UpdateMyProfileOutput(
                id = profile.id.toString(),
                userId = profile.userId.toString(),
                nickname = profile.nickname.value,
                avatarUrl = profile.avatarUrl?.value,
                bio = profile.bio?.value,
            )
        }
    }
}
