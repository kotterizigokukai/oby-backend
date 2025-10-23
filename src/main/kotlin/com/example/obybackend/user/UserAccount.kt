package com.example.obybackend.user

import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("app_user")
data class UserAccount(
    @Id
    val id: UUID? = null,
    @Column("provider")
    val provider: AuthProvider = AuthProvider.GOOGLE,
    @Column("provider_subject")
    val providerSubject: String,
    @Column("email")
    val email: String,
    @Column("display_name")
    val displayName: String,
    @Column("avatar_url")
    val avatarUrl: String?,
    @Column("role")
    val role: UserRole = UserRole.USER,
    @Column("last_login_at")
    val lastLoginAt: Instant = Instant.now(),
    @Column("created_at")
    val createdAt: Instant = Instant.now(),
    @Column("updated_at")
    val updatedAt: Instant = Instant.now(),
) {
    fun withUpdatedProfile(
        email: String,
        displayName: String,
        avatarUrl: String?,
        loginTime: Instant,
    ): UserAccount = copy(
        email = email,
        displayName = displayName,
        avatarUrl = avatarUrl,
        lastLoginAt = loginTime,
        updatedAt = loginTime,
    )
}
