package com.example.academilink.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    val username: String,
    val email: String,
    val fullName: String,
    val institution: String,
    val fieldOfStudy: String,
    val studyYear: Int,
    val bio: String = ""
)