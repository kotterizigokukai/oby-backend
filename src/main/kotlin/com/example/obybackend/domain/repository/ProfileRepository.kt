package com.example.obybackend.domain.repository

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.value.AvatarUrl
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import java.util.UUID

interface ProfileRepository {
    fun findByUserId(userId: UUID): ProfileEntity?

    fun save(profile: ProfileEntity): ProfileEntity

    fun updateNicknameAndBio(
        userId: UUID,
        nickname: Nickname,
        bio: Bio?,
    ): ProfileEntity

    fun updateAvatarUrl(
        userId: UUID,
        avatarUrl: AvatarUrl,
    ): ProfileEntity

    fun deleteAvatarUrl(userId: UUID): ProfileEntity
}
