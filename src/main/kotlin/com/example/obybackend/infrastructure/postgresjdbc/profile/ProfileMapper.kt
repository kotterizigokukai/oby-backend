package com.example.obybackend.infrastructure.postgresjdbc.profile

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.value.AvatarUrl
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import org.springframework.stereotype.Component

/**
 * ProfileEntity ↔ ProfileTable 変換
 */
@Component
class ProfileMapper {
    fun toDomain(table: ProfileTable): ProfileEntity {
        return ProfileEntity(
            id = table.id,
            userId = table.userId,
            nickname = Nickname(table.nickname),
            avatarUrl = table.avatarUrl?.let { AvatarUrl(it) },
            bio = table.bio?.let { Bio(it) },
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
    }

    fun toTable(entity: ProfileEntity): ProfileTable {
        return ProfileTable(
            id = entity.id,
            userId = entity.userId,
            nickname = entity.nickname.value,
            avatarUrl = entity.avatarUrl?.value,
            bio = entity.bio?.value,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }
}
