package com.example.academilink.model

import com.google.gson.annotations.SerializedName

data class GroupSummary(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val course: String,
    val institution: String
)