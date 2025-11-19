package com.example.obybackend.domain.repository

interface StorageService {
    fun upload(
        key: String,
        data: ByteArray,
        contentType: String,
    ): String

    fun delete(key: String)

    fun getPublicUrl(key: String): String
}
