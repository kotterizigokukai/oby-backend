package com.example.obybackend.usecase.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 自分のプロフィール取得ユースケース
 */
@Service
class GetMyProfileUseCase(
    private val profileRepository: ProfileRepository,
) {
    @Transactional(readOnly = true)
    fun execute(userId: UUID): GetMyProfileOutput {
        val profile =
            profileRepository.findByUserId(userId)
                ?: throw ProfileNotFoundException(userId.toString())

        return GetMyProfileOutput.from(profile)
    }
}

/**
 * 自分のプロフィール取得Output
 */
data class GetMyProfileOutput(
    val id: String,
    val userId: String,
    val nickname: String,
    val avatarUrl: String?,
    val bio: String?,
) {
    companion object {
        fun from(profile: ProfileEntity): GetMyProfileOutput {
            return GetMyProfileOutput(
                id = profile.id.toString(),
                userId = profile.userId.toString(),
                nickname = profile.nickname.value,
                avatarUrl = profile.avatarUrl?.value,
                bio = profile.bio?.value,
            )
        }
    }
}
