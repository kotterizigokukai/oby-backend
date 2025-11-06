package com.example.obybackend.domain.user

/**
 * ユーザーアカウントへアクセスするためのリポジトリポート。
 * 実装はインフラ層で行い、ドメイン層から外部技術へ依存しない。
 */
interface UserAccountRepository {
    fun findByProviderAndSubject(
        provider: AuthProvider,
        providerSubject: String,
    ): UserAccount?

    fun save(account: UserAccount): UserAccount
}
