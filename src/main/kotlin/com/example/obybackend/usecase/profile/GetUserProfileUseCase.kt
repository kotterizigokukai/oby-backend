package com.example.obybackend.usecase.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 他ユーザーのプロフィール取得ユースケース
 */
@Service
class GetUserProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    @Transactional(readOnly = true)
    fun execute(userId: UUID): GetUserProfileOutput {
        val profile =
            profileRepository.findByUserId(userId)
                ?: throw ProfileNotFoundException(userId.toString())

        return GetUserProfileOutput.from(profile)
    }
}

/**
 * 他ユーザーのプロフィール取得Output
 */
data class GetUserProfileOutput(
    val id: String,
    val userId: String,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
) {
    companion object {
        fun from(profile: ProfileEntity): GetUserProfileOutput {
            return GetUserProfileOutput(
                id = profile.id.toString(),
                userId = profile.userId.toString(),
                nickname = profile.nickname.value,
                avatarUrl = profile.avatarUrl?.value,
                bio = profile.bio?.value,
            )
        }
    }
}
