package com.example.obybackend.domain.entity

import com.example.obybackend.common.uuid.UuidGenerator
import com.example.obybackend.domain.value.AuthProvider
import com.example.obybackend.domain.value.UserRole
import java.time.Instant
import java.util.UUID

/**
 * 認証済みユーザーのアカウント情報を表すドメインエンティティ。
 * Springなど外部フレームワークの依存は持たず、純粋なドメインモデルとして扱う。
 */
data class UserEntity(
    val id: UUID = UuidGenerator.generate(),
    val provider: AuthProvider = AuthProvider.GOOGLE,
    val providerSubject: String,
    val email: String,
    val role: UserRole = UserRole.USER,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun withUpdatedProfile(
        email: String,
        updateTime: Instant,
    ): UserEntity =
        copy(
            email = email,
            updatedAt = updateTime,
        )
}
