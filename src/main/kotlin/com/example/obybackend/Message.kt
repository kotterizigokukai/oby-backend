package com.example.obybackend

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("messages")
@Schema(description = "メッセージエンティティ")
data class Message(
    @Schema(description = "メッセージ本文", example = "Hello, World!", required = true)
    val text: String,
    @Schema(description = "メッセージID（自動生成）", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    val id: UUID? = null,
)
