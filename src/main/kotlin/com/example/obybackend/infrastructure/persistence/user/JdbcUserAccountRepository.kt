package com.example.obybackend.infrastructure.persistence.user

import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.repository.UserAccountRepository
import com.example.obybackend.domain.value.AuthProvider
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserAccountCrudRepository : CrudRepository<UserAccountEntity, UUID> {
    fun findByProviderAndProviderSubject(
        provider: AuthProvider,
        providerSubject: String,
    ): UserAccountEntity?
}

/**
 * Spring Data JDBC を利用したユーザーアカウントリポジトリの実装。
 */
@Component
class JdbcUserAccountRepository(
    private val userAccountCrudRepository: UserAccountCrudRepository,
) : UserAccountRepository {
    override fun findByProviderAndSubject(
        provider: AuthProvider,
        providerSubject: String,
    ): UserEntity? =
        userAccountCrudRepository
            .findByProviderAndProviderSubject(provider, providerSubject)
            ?.toDomain()

    override fun save(account: UserEntity): UserEntity {
        val isNew = userAccountCrudRepository.existsById(account.id).not()
        val saved =
            userAccountCrudRepository.save(UserAccountEntity.fromDomain(account, isNew))
        return saved.toDomain()
    }
}
