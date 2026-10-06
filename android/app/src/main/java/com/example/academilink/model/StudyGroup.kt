package com.example.academilink.model

import com.google.gson.annotations.SerializedName

data class StudyGroup(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String,
    val institution: String,
    val course: String,
    val category: String,
    val ownerId: UserSummary,
    val members: List<UserSummary>
)