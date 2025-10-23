package com.example.obybackend.user

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface UserAccountRepository : CrudRepository<UserAccount, UUID> {
    fun findByProviderAndProviderSubject(provider: AuthProvider, providerSubject: String): UserAccount?
}
