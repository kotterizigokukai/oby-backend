package com.example.obybackend.domain.repository

import com.example.obybackend.domain.entity.UserEntity
import java.util.UUID

interface UserRepository {
    fun findById(id: UUID): UserEntity?

    fun findByGoogleSub(googleSub: String): UserEntity?

    fun findByEmail(email: String): UserEntity?

    fun save(user: UserEntity): UserEntity

    fun updateEmail(
        id: UUID,
        email: String,
    ): UserEntity
}
