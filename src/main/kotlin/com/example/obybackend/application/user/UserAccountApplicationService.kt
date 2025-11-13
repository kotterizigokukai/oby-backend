package com.example.obybackend.application.user

import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.repository.UserAccountRepository
import com.example.obybackend.domain.value.AuthProvider
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
    ): UserEntity? = repository.findByProviderAndSubject(provider, subject)

    @Transactional
    fun registerOrUpdateGoogleUser(
        subject: String,
        email: String,
    ): UserEntity {
        val now = Instant.now()
        val existingUser = repository.findByProviderAndSubject(AuthProvider.GOOGLE, subject)
        return if (existingUser == null) {
            val created =
                repository.save(
                    UserEntity(
                        providerSubject = subject,
                        email = email,
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
                        updateTime = now,
                    ),
                )
            logger.info("Updated Google user profile for subject {}", subject)
            updated
        }
    }
}
