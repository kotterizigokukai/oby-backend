package com.example.obybackend.domain.user

/**
 * 外部認証プロバイダの種別を表す値オブジェクト。
 * 現状はGoogleのみだが、将来的な拡張をここで吸収する。
 */
enum class AuthProvider {
    GOOGLE,
}
