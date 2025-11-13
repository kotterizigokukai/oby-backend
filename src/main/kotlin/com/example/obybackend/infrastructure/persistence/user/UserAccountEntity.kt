package com.example.obybackend.infrastructure.persistence.user

import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.value.AuthProvider
import com.example.obybackend.domain.value.UserRole
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("app_user")
data class UserAccountEntity(
    @Id
    @Column("id")
    private val aggregateId: UUID?,
    @Column("provider")
    val provider: AuthProvider,
    @Column("provider_subject")
    val providerSubject: String,
    @Column("email")
    val email: String,
    @Column("role")
    val role: UserRole,
    @Column("created_at")
    val createdAt: Instant,
    @Column("updated_at")
    val updatedAt: Instant,
    @Transient
    // Persistable#isNew を制御してINSERT/UPDATEを切り替えるフラグ
    private val newAggregate: Boolean = false,
) : Persistable<UUID> {
    override fun getId(): UUID? = aggregateId

    override fun isNew(): Boolean = newAggregate

    fun toDomain(): UserEntity =
        UserEntity(
            id = aggregateId ?: error("Persisted user must have an id"),
            provider = provider,
            providerSubject = providerSubject,
            email = email,
            role = role,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    companion object {
        fun fromDomain(
            userAccount: UserEntity,
            isNew: Boolean,
        ): UserAccountEntity =
            UserAccountEntity(
                aggregateId = userAccount.id,
                provider = userAccount.provider,
                providerSubject = userAccount.providerSubject,
                email = userAccount.email,
                role = userAccount.role,
                createdAt = userAccount.createdAt,
                updatedAt = userAccount.updatedAt,
                newAggregate = isNew,
            )
    }
}
