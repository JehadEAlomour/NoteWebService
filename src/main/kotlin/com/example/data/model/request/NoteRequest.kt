package com.example.data.model.request

data class NoteRequest(
    val email: String,
    val title: String,
    val body: String,
    val color: String,
    val image: String
)