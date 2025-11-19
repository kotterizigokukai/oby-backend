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

    /**
     * URLからストレージキーを抽出
     *
     * MinIO: http://localhost:9000/bucket/avatars/xxx.jpg
     * S3: https://bucket.s3.amazonaws.com/avatars/xxx.jpg
     * → avatars/xxx.jpg を抽出
     */
    fun extractStorageKey(): String {
        return value.substringAfter("avatars/").let { "avatars/$it" }
    }

    override fun toString(): String = value

    companion object {
        fun isValid(value: String): Boolean {
            return value.isNotBlank()
        }
    }
}
