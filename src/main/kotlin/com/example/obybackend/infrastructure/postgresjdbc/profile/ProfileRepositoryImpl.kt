package com.example.obybackend.infrastructure.postgresjdbc.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.value.AvatarUrl
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

/**
 * ProfileRepository実装
 */
@Repository
class ProfileRepositoryImpl(
    private val profileJdbcRepository: ProfileJdbcRepository,
    private val mapper: ProfileMapper,
) : ProfileRepository {
    override fun findByUserId(userId: UUID): ProfileEntity? {
        return profileJdbcRepository.findByUserId(userId)?.let { mapper.toDomain(it) }
    }

    override fun save(profile: ProfileEntity): ProfileEntity {
        val table = mapper.toTable(profile)
        profileJdbcRepository.upsert(
            id = table.id ?: UUID.randomUUID(),
            userId = table.userId,
            nickname = table.nickname,
            avatarUrl = table.avatarUrl,
            bio = table.bio,
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
        // UPSERTの後、再取得
        return profileJdbcRepository.findByUserId(table.userId)?.let { mapper.toDomain(it) }
            ?: throw ProfileNotFoundException(table.userId.toString())
    }

    override fun updateNicknameAndBio(
        userId: UUID,
        nickname: Nickname,
        bio: Bio?,
    ): ProfileEntity {
        val existing =
            profileJdbcRepository.findByUserId(userId)
                ?: throw ProfileNotFoundException(userId.toString())

        profileJdbcRepository.upsert(
            id = existing.id ?: throw IllegalStateException("Profile ID must not be null"),
            userId = existing.userId,
            nickname = nickname.value,
            avatarUrl = existing.avatarUrl,
            bio = bio?.value,
            createdAt = existing.createdAt,
            updatedAt = LocalDateTime.now(),
        )

        return profileJdbcRepository.findByUserId(userId)?.let { mapper.toDomain(it) }
            ?: throw ProfileNotFoundException(userId.toString())
    }

    override fun updateAvatarUrl(
        userId: UUID,
        avatarUrl: AvatarUrl,
    ): ProfileEntity {
        val existing =
            profileJdbcRepository.findByUserId(userId)
                ?: throw ProfileNotFoundException(userId.toString())

        profileJdbcRepository.upsert(
            id = existing.id ?: throw IllegalStateException("Profile ID must not be null"),
            userId = existing.userId,
            nickname = existing.nickname,
            avatarUrl = avatarUrl.value,
            bio = existing.bio,
            createdAt = existing.createdAt,
            updatedAt = LocalDateTime.now(),
        )

        return profileJdbcRepository.findByUserId(userId)?.let { mapper.toDomain(it) }
            ?: throw ProfileNotFoundException(userId.toString())
    }

    override fun deleteAvatarUrl(userId: UUID): ProfileEntity {
        val existing =
            profileJdbcRepository.findByUserId(userId)
                ?: throw ProfileNotFoundException(userId.toString())

        profileJdbcRepository.upsert(
            id = existing.id ?: throw IllegalStateException("Profile ID must not be null"),
            userId = existing.userId,
            nickname = existing.nickname,
            avatarUrl = null,
            bio = existing.bio,
            createdAt = existing.createdAt,
            updatedAt = LocalDateTime.now(),
        )

        return profileJdbcRepository.findByUserId(userId)?.let { mapper.toDomain(it) }
            ?: throw ProfileNotFoundException(userId.toString())
    }
}
