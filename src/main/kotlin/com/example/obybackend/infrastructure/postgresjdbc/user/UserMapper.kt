package com.example.obybackend.infrastructure.postgresjdbc.user

import com.example.obybackend.domain.entity.UserEntity
import org.springframework.stereotype.Component

/**
 * UserEntity ↔ UserTable 変換
 */
@Component
class UserMapper {
    fun toDomain(table: UserTable): UserEntity {
        return UserEntity(
            id = table.id,
            googleSub = table.googleSub,
            email = table.email,
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
    }

    fun toTable(entity: UserEntity): UserTable {
        return UserTable(
            id = entity.id,
            googleSub = entity.googleSub,
            email = entity.email,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }
}
