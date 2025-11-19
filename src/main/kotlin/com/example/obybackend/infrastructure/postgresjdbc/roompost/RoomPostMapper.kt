package com.example.obybackend.infrastructure.postgresjdbc.roompost

import com.example.obybackend.domain.entity.RoomPostEntity
import com.example.obybackend.domain.value.RoomPostDescription
import com.example.obybackend.domain.value.RoomPostTitle
import org.springframework.stereotype.Component

/**
 * RoomPostEntity ↔ RoomPostTable 変換
 */
@Component
class RoomPostMapper {
    fun toDomain(table: RoomPostTable): RoomPostEntity {
        return RoomPostEntity(
            id = table.id,
            userId = table.userId,
            title = RoomPostTitle(table.title),
            imageUrl = table.imageUrl,
            description = table.description?.let { RoomPostDescription(it) },
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
    }

    fun toTable(entity: RoomPostEntity): RoomPostTable {
        return RoomPostTable(
            id = entity.id,
            userId = entity.userId,
            title = entity.title.value,
            imageUrl = entity.imageUrl,
            description = entity.description?.value,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }
}
