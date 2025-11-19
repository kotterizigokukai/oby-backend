package com.example.obybackend.infrastructure.postgresjdbc.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.repository.ProfileRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * ProfileRepository実装
 *
 * シンプルなデータ永続化のみを担当
 * ビジネスロジック（タイムスタンプ更新等）はユースケース層で行う
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
            id = table.id,
            userId = table.userId,
            nickname = table.nickname,
            avatarUrl = table.avatarUrl,
            bio = table.bio,
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
        // UPSERTの後、再取得して最新状態を返す
        return profileJdbcRepository.findByUserId(table.userId)?.let { mapper.toDomain(it) }
            ?: throw ProfileNotFoundException(table.userId.toString())
    }
}
