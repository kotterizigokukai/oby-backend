package com.example.obybackend.domain.repository

import com.example.obybackend.domain.entity.UserEntity
import com.example.obybackend.domain.value.AuthProvider

/**
 * ユーザーアカウントへアクセスするためのリポジトリポート。
 * 実装はインフラ層で行い、ドメイン層から外部技術へ依存しない。
 */
interface UserAccountRepository {
    fun findByProviderAndSubject(
        provider: AuthProvider,
        providerSubject: String,
    ): UserEntity?

    fun save(account: UserEntity): UserEntity
}
