package com.example.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class UserRequest(
    @SerialName("EMAIL")
    val email: String,
    @SerialName("NAME")
    val name: String,
    @SerialName("PASSWORD")
    val password: String
)