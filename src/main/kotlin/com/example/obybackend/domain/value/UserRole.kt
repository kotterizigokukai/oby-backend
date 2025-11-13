package com.example.obybackend.domain.value

/**
 * アプリケーション内の役割を表す列挙体。
 * Spring Securityの権限表現とマッピングするためのヘルパを備える。
 */
enum class UserRole {
    USER,
    ADMIN,
    ;

    fun asAuthority(): String = "ROLE_$name"
}
