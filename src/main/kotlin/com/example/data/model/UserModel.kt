package com.example.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
data class UserModel(
    @SerialName("EMAIL")
    val email: String,
    @SerialName("NAME")
    val userName: String,
    @SerialName("PASSWORD")
    val hashPassword: String

)
