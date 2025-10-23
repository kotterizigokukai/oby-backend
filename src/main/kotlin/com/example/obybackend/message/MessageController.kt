package com.example.obybackend.message

import java.net.URI
import java.util.UUID
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val service: MessageService,
) {
    @GetMapping
    fun listMessages(): ResponseEntity<List<Message>> = ResponseEntity.ok(service.findMessages())

    @GetMapping("/{id}")
    fun findMessage(
        @PathVariable id: String,
    ): ResponseEntity<Message> {
        val message = service.findMessageById(UUID.fromString(id)) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(message)
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun post(
        @RequestBody message: Message,
    ): ResponseEntity<Message> {
        val savedMessage = service.save(message)
        return ResponseEntity.created(URI("/api/messages/${savedMessage.id}")).body(savedMessage)
    }
}
