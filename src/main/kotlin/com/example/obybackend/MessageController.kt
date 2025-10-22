package com.example.obybackend

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/")
@Tag(name = "Messages", description = "メッセージ管理API")
class MessageController(
    private val service: MessageService,
) {
    @GetMapping
    @Operation(operationId = "listMessages", summary = "メッセージ一覧取得")
    fun listMessages() = ResponseEntity.ok(service.findMessages())

    @PostMapping
    @Operation(operationId = "createMessage", summary = "メッセージ作成")
    fun post(
        @RequestBody message: Message,
    ): ResponseEntity<Message> {
        val savedMessage = service.save(message)
        return ResponseEntity.created(URI("/${savedMessage.id}")).body(savedMessage)
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getMessageById", summary = "メッセージ取得")
    fun getMessage(
        @PathVariable id: java.util.UUID,
    ): ResponseEntity<Message> = service.findMessageById(id).toResponseEntity()

    private fun Message?.toResponseEntity(): ResponseEntity<Message> =
        // If the message is null (not found), set response code to 404
        this?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()
}
