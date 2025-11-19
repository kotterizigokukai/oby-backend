package com.example.obybackend.domain.value

/**
 * 部屋投稿タイトル値オブジェクト
 *
 * @property value タイトル文字列 (1-100文字、空白不可)
 * @throws IllegalArgumentException バリデーションに失敗した場合
 */
data class RoomPostTitle(val value: String) {
    init {
        require(value.isNotBlank()) { "Title cannot be blank" }
        require(value.length <= MAX_LENGTH) { "Title must not exceed $MAX_LENGTH characters" }
    }

    override fun toString(): String = value

    companion object {
        private const val MAX_LENGTH = 100

        fun isValid(value: String): Boolean {
            return value.isNotBlank() && value.length <= MAX_LENGTH
        }
    }
}
