package com.example.obybackend.usecase.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * プロフィール更新ユースケース
 */
@Service
class UpdateMyProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    @Transactional
    fun execute(input: UpdateMyProfileInput): UpdateMyProfileOutput {
        val nickname = Nickname(input.nickname)
        val bio = input.bio?.let { Bio(it) }

        val updatedProfile =
            profileRepository.updateNicknameAndBio(
                userId = input.userId,
                nickname = nickname,
                bio = bio,
            )

        return UpdateMyProfileOutput.from(updatedProfile)
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
