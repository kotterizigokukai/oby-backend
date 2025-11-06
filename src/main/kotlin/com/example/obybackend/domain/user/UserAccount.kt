package com.example.obybackend.domain.user

import com.example.obybackend.common.uuid.UuidGenerator
import java.time.Instant
import java.util.UUID

/**
 * 認証済みユーザーのアカウント情報を表すドメインエンティティ。
 * Springなど外部フレームワークの依存は持たず、純粋なドメインモデルとして扱う。
 */
data class UserAccount(
    val id: UUID = UuidGenerator.generate(),
    val provider: AuthProvider = AuthProvider.GOOGLE,
    val providerSubject: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String?,
    val role: UserRole = UserRole.USER,
    val lastLoginAt: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    fun withUpdatedProfile(
        email: String,
        displayName: String,
        avatarUrl: String?,
        loginTime: Instant,
    ): UserAccount =
        copy(
            email = email,
            displayName = displayName,
            avatarUrl = avatarUrl,
            lastLoginAt = loginTime,
            updatedAt = loginTime,
        )
}
