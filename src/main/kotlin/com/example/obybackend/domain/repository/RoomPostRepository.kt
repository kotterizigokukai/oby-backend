package com.example.obybackend.domain.repository

import com.example.obybackend.domain.entity.RoomPostEntity
import java.util.UUID

interface RoomPostRepository {
    fun save(roomPost: RoomPostEntity): RoomPostEntity

    fun findById(id: UUID): RoomPostEntity?

    fun findAll(
        cursor: UUID?,
        limit: Int,
    ): List<RoomPostEntity>

    fun deleteById(id: UUID)

    fun existsByIdAndUserId(
        id: UUID,
        userId: UUID,
    ): Boolean
}
