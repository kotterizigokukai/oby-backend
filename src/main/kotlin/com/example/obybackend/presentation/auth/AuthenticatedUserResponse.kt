package com.example.obybackend.presentation.auth

data class AuthenticatedUserResponse(
    val id: String?,
    val email: String,
    val name: String,
    val pictureUrl: String?,
    val roles: List<String>,
)
