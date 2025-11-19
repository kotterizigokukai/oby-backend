package com.example.obybackend.common.timestamp

import org.springframework.stereotype.Component
import java.time.Instant

/**
 * タイムスタンプ生成ユーティリティ
 *
 * システム全体で一貫したタイムスタンプ生成を保証する
 * すべてのタイムスタンプはUTCで生成される
 */
@Component
class TimestampGenerator {
    /**
     * 現在のタイムスタンプをUTCで生成
     *
     * @return 現在時刻のInstant (UTC)
     */
    fun now(): Instant = Instant.now()
}
