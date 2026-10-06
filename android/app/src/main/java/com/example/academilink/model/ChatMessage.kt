package com.example.academilink.model

data class ChatMessage(
    val senderId: String,
    val receiverId: String,
    val content: String,
    val createdAt: String = ""
)