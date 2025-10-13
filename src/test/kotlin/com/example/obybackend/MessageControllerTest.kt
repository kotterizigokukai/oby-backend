package com.example.obybackend

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 201 when creating a new message`() {
        val requestBody = """{"text": "Hello, World!"}"""

        mockMvc.perform(
            post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody),
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `should return 200 when getting all messages`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk)
    }
}
