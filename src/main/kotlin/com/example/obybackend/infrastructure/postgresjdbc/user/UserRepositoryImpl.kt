package com.example.obybackend.infrastructure.postgresjdbc.user

import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.repository.UserRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

/**
 * UserRepository実装
 */
@Repository
class UserRepositoryImpl(
    private val userJdbcRepository: UserJdbcRepository,
    private val mapper: UserMapper,
) : UserRepository {
    override fun findById(id: UUID): UserEntity? {
        return userJdbcRepository.findByUserId(id)?.let { mapper.toDomain(it) }
    }

    override fun findByGoogleSub(googleSub: String): UserEntity? {
        return userJdbcRepository.findByGoogleSub(googleSub)?.let { mapper.toDomain(it) }
    }

    override fun findByEmail(email: String): UserEntity? {
        return userJdbcRepository.findByEmail(email)?.let { mapper.toDomain(it) }
    }

    override fun save(user: UserEntity): UserEntity {
        val table = mapper.toTable(user)
        userJdbcRepository.upsert(
            id = table.id,
            googleSub = table.googleSub,
            email = table.email,
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
        // UPSERTの後、再取得
        return userJdbcRepository.findByGoogleSub(table.googleSub)?.let { mapper.toDomain(it) }
            ?: throw IllegalStateException("Failed to save user")
    }

    override fun updateEmail(
        id: UUID,
        email: String,
    ): UserEntity {
        val existing =
            userJdbcRepository.findByUserId(id)
                ?: throw IllegalStateException("User not found: $id")

        userJdbcRepository.upsert(
            id = existing.id,
            googleSub = existing.googleSub,
            email = email,
            createdAt = existing.createdAt,
            updatedAt = LocalDateTime.now(),
        )

        return userJdbcRepository.findByUserId(id)?.let { mapper.toDomain(it) }
            ?: throw IllegalStateException("Failed to update user email")
    }
}
