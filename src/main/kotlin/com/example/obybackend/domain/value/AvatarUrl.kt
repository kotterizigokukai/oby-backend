package com.example.obybackend.domain.value

import com.example.obybackend.domain.exception.InvalidAvatarUrlException

/**
 * アバターURL値オブジェクト
 *
 * @property value アバターURL (空白不可)
 * @throws InvalidAvatarUrlException バリデーションに失敗した場合
 */
data class AvatarUrl(val value: String) {
    init {
        if (!isValid(value)) {
            throw InvalidAvatarUrlException("AvatarUrl cannot be blank")
        }
    }

    override fun toString(): String = value

    companion object {
        fun isValid(value: String): Boolean {
            return value.isNotBlank()
        }
    }
}
