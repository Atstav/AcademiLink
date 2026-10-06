package com.example.academilink.model

import com.google.gson.annotations.SerializedName

data class UserSummary(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val fullName: String
)