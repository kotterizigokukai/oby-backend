package com.example.obybackend.user

enum class UserRole {
    USER,
    ADMIN,
    ;

    // 役割に応じたROLE_プレフィックス付き権限名を一元的に生成する
    fun asAuthority(): String = "ROLE_$name"
}
