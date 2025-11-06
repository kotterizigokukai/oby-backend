package com.example.obybackend.infrastructure.persistence.user

import com.example.obybackend.domain.user.AuthProvider
import com.example.obybackend.domain.user.UserAccount
import com.example.obybackend.domain.user.UserAccountRepository
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
    ): UserAccount? =
        userAccountCrudRepository
            .findByProviderAndProviderSubject(provider, providerSubject)
            ?.toDomain()

    override fun save(account: UserAccount): UserAccount {
        val saved =
            userAccountCrudRepository.save(UserAccountEntity.fromDomain(account))
        return saved.toDomain()
    }
}
