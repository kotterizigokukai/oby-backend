package com.example.obybackend.common.uuid

import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID

/**
 * UUID生成のユーティリティクラス
 */
object UuidGenerator {
    /**
     * UUID v7を生成
     */
    fun generate(): UUID = UuidCreator.getTimeOrderedEpoch()

    /**
     * 指定された文字列からUUIDをパース
     */
    fun fromString(uuidString: String): UUID = UUID.fromString(uuidString)
}
