package com.example.obybackend.message

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val repository: MessageRepository,
) {
    fun findMessages(): List<Message> = repository.findAll().toList()

    fun findMessageById(id: UUID): Message? = repository.findByIdOrNull(id)

    fun save(message: Message): Message = repository.save(message)
}
