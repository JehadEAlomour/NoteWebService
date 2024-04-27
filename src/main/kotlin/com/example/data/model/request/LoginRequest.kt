package com.example.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("EMAIL")
    val email: String,
    @SerialName("PASSWORD")
    val password: String
)
