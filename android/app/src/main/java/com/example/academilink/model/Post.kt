package com.example.academilink.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("_id")
    val id: String,
    val content: String,
    val category: String,
    val authorId: UserSummary,
    val groupId: GroupSummary,
    val createdAt: String,
    val updatedAt: String
)