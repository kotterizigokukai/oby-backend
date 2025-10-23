package com.example.obybackend.user

import java.time.Instant
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAccountService(
    private val repository: UserAccountRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findBySubject(provider: AuthProvider, subject: String): UserAccount? =
        repository.findByProviderAndProviderSubject(provider, subject)

    @Transactional
    fun registerGoogleUser(
        subject: String,
        email: String,
        displayName: String,
        avatarUrl: String?,
    ): UserAccount {
        val now = Instant.now()
        val existingUser = repository.findByProviderAndProviderSubject(AuthProvider.GOOGLE, subject)
        return if (existingUser == null) {
            val created = repository.save(
                UserAccount(
                    providerSubject = subject,
                    email = email,
                    displayName = displayName,
                    avatarUrl = avatarUrl,
                    lastLoginAt = now,
                    createdAt = now,
                    updatedAt = now,
                ),
            )
            logger.info("Created new Google user with subject {}", subject)
            created
        } else {
            val updated = repository.save(
                existingUser.withUpdatedProfile(
                    email = email,
                    displayName = displayName,
                    avatarUrl = avatarUrl,
                    loginTime = now,
                ),
            )
            logger.info("Updated Google user profile for subject {}", subject)
            updated
        }
    }
}
