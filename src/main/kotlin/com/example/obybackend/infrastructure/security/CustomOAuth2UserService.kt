package com.example.obybackend.infrastructure.security

import com.example.obybackend.domain.entity.ProfileEntity
import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.repository.ProfileRepository
import com.example.obybackend.domain.repository.UserRepository
import com.example.obybackend.domain.value.Nickname
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        val googleSub =
            oauth2User.getAttribute<String>("sub")
                ?: throw IllegalStateException("Google sub not found")
        val email =
            oauth2User.getAttribute<String>("email")
                ?: throw IllegalStateException("Email not found")
        val name = oauth2User.getAttribute<String>("name") ?: email.substringBefore("@")

        // 既存ユーザーを検索または新規作成
        val user =
            userRepository.findByGoogleSub(googleSub)
                ?: createNewUser(googleSub, email, name)

        // emailが変更されている場合は更新
        if (user.email != email) {
            userRepository.updateEmail(user.id, email)
        }

        return oauth2User
    }

    @Transactional
    private fun createNewUser(
        googleSub: String,
        email: String,
        name: String,
    ): UserEntity {
        // 新規ユーザーを作成
        val newUser =
            UserEntity(
                googleSub = googleSub,
                email = email,
            )
        val savedUser = userRepository.save(newUser)

        // 空のプロフィールを自動作成
        // 初期値としてGoogleの名前を使用
        val newProfile =
            ProfileEntity(
                userId = savedUser.id,
                nickname = Nickname(name),
                avatarUrl = null,
                bio = null,
            )
        profileRepository.save(newProfile)

        return savedUser
    }
}
