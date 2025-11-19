package com.example.obybackend.domain.value

/**
 * 部屋投稿説明文値オブジェクト
 *
 * @property value 説明文文字列 (最大1000文字、空文字列・nullは許可)
 * @throws IllegalArgumentException バリデーションに失敗した場合
 */
data class RoomPostDescription(val value: String) {
    init {
        require(value.length <= MAX_LENGTH) { "Description must not exceed $MAX_LENGTH characters" }
    }

    override fun toString(): String = value

    companion object {
        private const val MAX_LENGTH = 1000

        fun isValid(value: String): Boolean {
            return value.length <= MAX_LENGTH
        }
    }
}
