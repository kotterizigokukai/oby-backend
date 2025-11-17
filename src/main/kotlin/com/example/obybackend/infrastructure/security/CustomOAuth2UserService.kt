package com.example.obybackend.infrastructure.security

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.UserRepository
import com.example.obybackend.domain.value.Bio
import com.example.obybackend.domain.value.Nickname
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

/**
 * カスタムOAuth2ユーザーサービス
 *
 * Google認証後にユーザー情報を処理
 * - 初回ログイン時にusersテーブルとprofilesテーブルにレコードを作成
 * - 既存ユーザーの場合はemailを更新
 */
@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
) : DefaultOAuth2UserService() {
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oauth2User = super.loadUser(userRequest)

        // Googleから取得した情報
        val googleSub = oauth2User.getAttribute<String>("sub")
            ?: throw IllegalStateException("Google sub not found")
        val email = oauth2User.getAttribute<String>("email")
            ?: throw IllegalStateException("Email not found")
        val name = oauth2User.getAttribute<String>("name") ?: email.substringBefore("@")

        // 既存ユーザーを検索または新規作成
        val user = userRepository.findByGoogleSub(googleSub)
            ?: createNewUser(googleSub, email, name)

        // emailが変更されている場合は更新
        if (user.email != email) {
            userRepository.updateEmail(user.id, email)
        }

        return oauth2User
    }

    private fun createNewUser(
        googleSub: String,
        email: String,
        name: String,
    ): UserEntity {
        val now = LocalDateTime.now()

        // 新規ユーザーを作成
        val newUser = UserEntity(
            id = UUID.randomUUID(),
            googleSub = googleSub,
            email = email,
            createdAt = now,
            updatedAt = now,
        )
        val savedUser = userRepository.save(newUser)

        // 空のプロフィールを自動作成
        val newProfile = ProfileEntity(
            id = UUID.randomUUID(),
            userId = savedUser.id,
            nickname = Nickname(name), // 初期値としてGoogleの名前を使用
            avatarUrl = null,
            bio = null,
            createdAt = now,
            updatedAt = now,
        )
        profileRepository.save(newProfile)

        return savedUser
    }
}
