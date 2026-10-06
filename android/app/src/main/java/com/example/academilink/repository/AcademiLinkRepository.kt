package com.example.academilink.repository

import com.example.academilink.network.LoginRequest
import com.example.academilink.network.RetrofitClient
import com.example.academilink.network.RegisterRequest
import com.example.academilink.network.UpdateProfileRequest
import com.example.academilink.network.CreateGroupRequest
import com.example.academilink.network.UpdateGroupRequest
import com.example.academilink.network.CreatePostRequest
import com.example.academilink.network.UpdatePostRequest
class AcademiLinkRepository {

    suspend fun login(email: String, password: String) =
        RetrofitClient.apiService.login(
            LoginRequest(email, password)
        )

    suspend fun getGroups() =
        RetrofitClient.apiService.getGroups()

    suspend fun getPosts() =
        RetrofitClient.apiService.getPosts()

    suspend fun createPost(
        token: String,
        content: String,
        category: String,
        groupId: String
    ) = RetrofitClient.apiService.createPost(
        authorization = "Bearer $token",
        request = CreatePostRequest(
            content = content,
            category = category,
            groupId = groupId
        )
    )

    suspend fun updatePost(
        token: String,
        postId: String,
        content: String,
        category: String
    ) = RetrofitClient.apiService.updatePost(
        authorization = "Bearer $token",
        postId = postId,
        request = UpdatePostRequest(
            content = content,
            category = category
        )
    )

    suspend fun deletePost(
        token: String,
        postId: String
    ) = RetrofitClient.apiService.deletePost(
        authorization = "Bearer $token",
        postId = postId
    )

    suspend fun register(
        username: String,
        email: String,
        password: String,
        fullName: String,
        institution: String,
        fieldOfStudy: String,
        studyYear: Int,
        bio: String = ""
    ) = RetrofitClient.apiService.register(
        RegisterRequest(
            username = username,
            email = email,
            password = password,
            fullName = fullName,
            institution = institution,
            fieldOfStudy = fieldOfStudy,
            studyYear = studyYear,
            bio = bio
        )
    )

    suspend fun updateProfile(
        token: String,
        fullName: String,
        institution: String,
        fieldOfStudy: String,
        studyYear: Int,
        bio: String
    ) = RetrofitClient.apiService.updateProfile(
        authorization = "Bearer $token",
        request = UpdateProfileRequest(
            fullName = fullName,
            institution = institution,
            fieldOfStudy = fieldOfStudy,
            studyYear = studyYear,
            bio = bio
        )
    )
    suspend fun deleteProfile(
        token: String
    ) = RetrofitClient.apiService.deleteProfile(
        authorization = "Bearer $token"
    )

    suspend fun searchGroups(
        institution: String,
        course: String,
        category: String
    ) = RetrofitClient.apiService.searchGroups(
        institution = institution,
        course = course,
        category = category
    )

    suspend fun createGroup(
        token: String,
        name: String,
        description: String,
        institution: String,
        course: String,
        category: String
    ) = RetrofitClient.apiService.createGroup(
        authorization = "Bearer $token",
        request = CreateGroupRequest(
            name = name,
            description = description,
            institution = institution,
            course = course,
            category = category
        )
    )

    suspend fun deleteGroup(
        token: String,
        groupId: String
    ) = RetrofitClient.apiService.deleteGroup(
        authorization = "Bearer $token",
        groupId = groupId
    )

    suspend fun updateGroup(
        token: String,
        groupId: String,
        name: String,
        description: String,
        institution: String,
        course: String,
        category: String
    ) = RetrofitClient.apiService.updateGroup(
        authorization = "Bearer $token",
        groupId = groupId,
        request = UpdateGroupRequest(
            name = name,
            description = description,
            institution = institution,
            course = course,
            category = category
        )
    )
    suspend fun joinGroup(
        token: String,
        groupId: String
    ) = RetrofitClient.apiService.joinGroup(
        authorization = "Bearer $token",
        groupId = groupId
    )

    suspend fun leaveGroup(
        token: String,
        groupId: String
    ) = RetrofitClient.apiService.leaveGroup(
        authorization = "Bearer $token",
        groupId = groupId
    )

    suspend fun searchPosts(
        groupId: String,
        category: String,
        dateFrom: String,
        dateTo: String
    ) = RetrofitClient.apiService.searchPosts(
        groupId = groupId,
        category = category,
        dateFrom = dateFrom,
        dateTo = dateTo
    )

    suspend fun getUsers() =
        RetrofitClient.apiService.getUsers()

    suspend fun searchUsers(
        institution: String,
        fieldOfStudy: String,
        studyYear: String
    ) = RetrofitClient.apiService.searchUsers(
        institution = institution,
        fieldOfStudy = fieldOfStudy,
        studyYear = studyYear
    )
}