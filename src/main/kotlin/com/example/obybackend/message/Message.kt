package com.example.obybackend.message

import java.time.Instant
import java.util.UUID
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("message")
data class Message(
    @Id
    val id: UUID? = null,
    @Column("body")
    val body: String,
    @Column("author")
    val author: String,
    @Column("created_at")
    val createdAt: Instant = Instant.now(),
)
