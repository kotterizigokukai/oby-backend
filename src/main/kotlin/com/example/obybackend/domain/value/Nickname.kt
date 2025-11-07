package com.example.obybackend.domain.value

import com.example.obybackend.domain.exception.InvalidNicknameException

/**
 * ニックネーム値オブジェクト
 *
 * @property value ニックネーム文字列 (1-50文字、空白不可)
 * @throws InvalidNicknameException バリデーションに失敗した場合
 */
data class Nickname(val value: String) {
    init {
        if (!isValid(value)) {
            throw InvalidNicknameException("Nickname must be between 1 and 50 characters and cannot be blank")
        }
    }

    override fun toString(): String = value

    companion object {
        private const val MAX_LENGTH = 50
        fun isValid(value: String): Boolean {
            return value.isNotBlank() && value.length <= MAX_LENGTH
        }
    }
}
