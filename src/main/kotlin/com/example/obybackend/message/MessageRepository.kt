package com.example.obybackend.message

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface MessageRepository : CrudRepository<Message, UUID>
