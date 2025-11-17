package com.example.obybackend.common.uuid

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@DisplayName("UuidGenerator")
class UuidGeneratorTest {
    @Nested
    @DisplayName("generate()")
    inner class Generate {
        @Test
        @DisplayName("UUIDを生成できる")
        fun generatesUuid() {
            val uuid = UuidGenerator.generate()
            assertNotNull(uuid)
        }

        @Test
        @DisplayName("生成されるUUIDはユニークである")
        fun generatesUniqueUuids() {
            val uuid1 = UuidGenerator.generate()
            val uuid2 = UuidGenerator.generate()

            assertNotEquals(uuid1, uuid2)
        }
    }

    @Nested
    @DisplayName("fromString()")
    inner class FromString {
        @Test
        @DisplayName("有効なUUID文字列をパースできる")
        fun parsesValidUuidString() {
            val originalUuid = UuidGenerator.generate()
            val parsedUuid = UuidGenerator.fromString(originalUuid.toString())

            assertEquals(originalUuid, parsedUuid)
        }

        @Test
        @DisplayName("無効なUUID文字列の場合は例外をスローする")
        fun throwsExceptionForInvalidString() {
            assertThrows<IllegalArgumentException> {
                UuidGenerator.fromString("invalid-uuid-string")
            }
        }
    }
}
