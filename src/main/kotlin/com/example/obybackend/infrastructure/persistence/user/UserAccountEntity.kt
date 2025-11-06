package com.example.obybackend.infrastructure.persistence.user

import com.example.obybackend.domain.user.AuthProvider
import com.example.obybackend.domain.user.UserAccount
import com.example.obybackend.domain.user.UserRole
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("app_user")
data class UserAccountEntity(
    @Id
    val id: UUID? = null,
    @Column("provider")
    val provider: AuthProvider,
    @Column("provider_subject")
    val providerSubject: String,
    @Column("email")
    val email: String,
    @Column("display_name")
    val displayName: String,
    @Column("avatar_url")
    val avatarUrl: String?,
    @Column("role")
    val role: UserRole,
    @Column("last_login_at")
    val lastLoginAt: Instant,
    @Column("created_at")
    val createdAt: Instant,
    @Column("updated_at")
    val updatedAt: Instant,
) {
    fun toDomain(): UserAccount =
        UserAccount(
            id = id,
            provider = provider,
            providerSubject = providerSubject,
            email = email,
            displayName = displayName,
            avatarUrl = avatarUrl,
            role = role,
            lastLoginAt = lastLoginAt,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun fromDomain(userAccount: UserAccount): UserAccountEntity =
            UserAccountEntity(
                id = userAccount.id,
                provider = userAccount.provider,
                providerSubject = userAccount.providerSubject,
                email = userAccount.email,
                displayName = userAccount.displayName,
                avatarUrl = userAccount.avatarUrl,
                role = userAccount.role,
                lastLoginAt = userAccount.lastLoginAt,
                createdAt = userAccount.createdAt,
                updatedAt = userAccount.updatedAt,
            )
    }
}
