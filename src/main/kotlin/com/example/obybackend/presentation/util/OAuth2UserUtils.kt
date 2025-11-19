package com.example.obybackend.presentation.util

import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.repository.UserRepository
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * OAuth2User関連のユーティリティクラス
 */
@Component
class OAuth2UserUtils(
    private val userRepository: UserRepository,
) {
    /**
     * OAuth2UserからUserEntityを取得
     *
     * @param oauth2User Spring SecurityのOAuth2User
     * @return UserEntity
     * @throws IllegalStateException Google subが取得できない、またはユーザーが見つからない場合
     */
    fun extractUser(oauth2User: OAuth2User): UserEntity {
        val googleSub =
            oauth2User.getAttribute<String>("sub")
                ?: throw IllegalStateException("Google sub not found in OAuth2User")

        return userRepository.findByGoogleSub(googleSub)
            ?: throw IllegalStateException("User not found")
    }

    /**
     * OAuth2UserからユーザーIDを取得
     *
     * @param oauth2User Spring SecurityのOAuth2User
     * @return ユーザーID (UUID)
     * @throws IllegalStateException Google subが取得できない、またはユーザーが見つからない場合
     */
    fun extractUserId(oauth2User: OAuth2User): UUID {
        return extractUser(oauth2User).id
    }
}
