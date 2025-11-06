package com.example.obybackend.application.user

import com.example.obybackend.domain.user.AuthProvider
import com.example.obybackend.domain.user.UserAccount
import com.example.obybackend.domain.user.UserAccountRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

/**
 * ユーザー認証・プロフィール更新に関連するユースケースを束ねるアプリケーションサービス。
 */
@Service
class UserAccountApplicationService(
    private val repository: UserAccountRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun findBySubject(
        provider: AuthProvider,
        subject: String,
    ): UserAccount? = repository.findByProviderAndSubject(provider, subject)

    @Transactional
    fun registerOrUpdateGoogleUser(
        subject: String,
        email: String,
        displayName: String,
        avatarUrl: String?,
    ): UserAccount {
        val now = Instant.now()
        val existingUser = repository.findByProviderAndSubject(AuthProvider.GOOGLE, subject)
        return if (existingUser == null) {
            val created =
                repository.save(
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
            val updated =
                repository.save(
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
