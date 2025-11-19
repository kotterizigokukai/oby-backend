package com.example.obybackend.infrastructure.postgresjdbc.roompost

import com.example.obybackend.domain.entity.RoomPostEntity
import com.example.obybackend.domain.repository.RoomPostRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * RoomPostRepository実装
 */
@Repository
class RoomPostRepositoryImpl(
    private val roomPostJdbcRepository: RoomPostJdbcRepository,
    private val mapper: RoomPostMapper,
) : RoomPostRepository {
    override fun save(roomPost: RoomPostEntity): RoomPostEntity {
        val table = mapper.toTable(roomPost)
        roomPostJdbcRepository.insert(
            id = table.id,
            userId = table.userId,
            title = table.title,
            imageUrl = table.imageUrl,
            description = table.description,
            createdAt = table.createdAt,
            updatedAt = table.updatedAt,
        )
        return roomPost
    }

    override fun findById(id: UUID): RoomPostEntity? {
        return roomPostJdbcRepository.findByRoomPostId(id)?.let { mapper.toDomain(it) }
    }

    override fun findAll(
        cursor: UUID?,
        limit: Int,
    ): List<RoomPostEntity> {
        return roomPostJdbcRepository.findAllWithCursor(cursor, limit)
            .map { mapper.toDomain(it) }
    }

    override fun deleteById(id: UUID) {
        roomPostJdbcRepository.deleteByRoomPostId(id)
    }

    override fun existsByIdAndUserId(
        id: UUID,
        userId: UUID,
    ): Boolean {
        return roomPostJdbcRepository.existsByIdAndUserId(id, userId)
    }
}
