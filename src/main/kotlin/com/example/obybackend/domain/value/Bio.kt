package com.example.obybackend.domain.value

import com.example.obybackend.domain.exception.InvalidBioException

/**
 * 自己紹介文値オブジェクト
 *
 * @property value 自己紹介文 (最大500文字)
 * @throws InvalidBioException バリデーションに失敗した場合
 */
data class Bio(val value: String) {
    init {
        if (!isValid(value)) {
            throw InvalidBioException("Bio must be at most 500 characters")
        }
    }

    override fun toString(): String = value

    companion object {
        private const val MAX_LENGTH = 500

        fun isValid(value: String): Boolean {
            return value.length <= MAX_LENGTH
        }
    }
}
