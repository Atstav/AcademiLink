package com.example.academilink.network

import com.example.academilink.model.Post
import com.example.academilink.model.StudyGroup
import com.example.academilink.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {

    @POST("api/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/users/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @GET("api/groups")
    suspend fun getGroups(): Response<GroupsResponse>

    @GET("api/posts")
    suspend fun getPosts(): Response<PostsResponse>

    @POST("api/posts")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Body request: CreatePostRequest
    ): Response<CreatePostResponse>

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Header("Authorization") authorization: String,
        @Path("id") postId: String,
        @Body request: UpdatePostRequest
    ): Response<UpdatePostResponse>

    @DELETE("api/posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") authorization: String,
        @Path("id") postId: String
    ): Response<Unit>

    @PUT("api/users/profile")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @DELETE("api/users/profile")
    suspend fun deleteProfile(
        @Header("Authorization") authorization: String
    ): Response<Unit>

    @GET("api/groups/search")
    suspend fun searchGroups(
        @Query("institution") institution: String,
        @Query("course") course: String,
        @Query("category") category: String
    ): Response<GroupsResponse>

    @POST("api/groups")
    suspend fun createGroup(
        @Header("Authorization") authorization: String,
        @Body request: CreateGroupRequest
    ): Response<CreateGroupResponse>

    @DELETE("api/groups/{id}")
    suspend fun deleteGroup(
        @Header("Authorization") authorization: String,
        @Path("id") groupId: String
    ): Response<Unit>

    @PUT("api/groups/{id}")
    suspend fun updateGroup(
        @Header("Authorization") authorization: String,
        @Path("id") groupId: String,
        @Body request: UpdateGroupRequest
    ): Response<UpdateGroupResponse>

    @POST("api/groups/{id}/join")
    suspend fun joinGroup(
        @Header("Authorization") authorization: String,
        @Path("id") groupId: String
    ): Response<Unit>

    @POST("api/groups/{id}/leave")
    suspend fun leaveGroup(
        @Header("Authorization") authorization: String,
        @Path("id") groupId: String
    ): Response<Unit>

    @GET("api/posts/search")
    suspend fun searchPosts(
        @Query("groupId") groupId: String,
        @Query("category") category: String,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Response<PostsResponse>

    @GET("api/users")
    suspend fun getUsers(): Response<UsersResponse>

    @GET("api/users/search")
    suspend fun searchUsers(
        @Query("institution") institution: String,
        @Query("fieldOfStudy") fieldOfStudy: String,
        @Query("studyYear") studyYear: String
    ): Response<UsersResponse>
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val user: User,
    val token: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String,
    val institution: String,
    val fieldOfStudy: String,
    val studyYear: Int,
    val bio: String = ""
)

data class RegisterResponse(
    val success: Boolean,
    val user: User,
    val token: String
)

data class GroupsResponse(
    val success: Boolean,
    val groups: List<StudyGroup>
)

data class PostsResponse(
    val success: Boolean,
    val posts: List<Post>
)

data class CreatePostRequest(
    val content: String,
    val category: String,
    val groupId: String
)

data class CreatePostResponse(
    val success: Boolean,
    val post: Post
)

data class UpdatePostRequest(
    val content: String,
    val category: String
)

data class UpdatePostResponse(
    val success: Boolean,
    val post: Post
)

data class UpdateProfileRequest(
    val fullName: String,
    val institution: String,
    val fieldOfStudy: String,
    val studyYear: Int,
    val bio: String
)

data class UpdateProfileResponse(
    val success: Boolean,
    val user: User
)

data class CreateGroupRequest(
    val name: String,
    val description: String,
    val institution: String,
    val course: String,
    val category: String
)

data class CreateGroupResponse(
    val success: Boolean,
    val group: StudyGroup
)

data class UpdateGroupRequest(
    val name: String,
    val description: String,
    val institution: String,
    val course: String,
    val category: String
)

data class UpdateGroupResponse(
    val success: Boolean,
    val group: StudyGroup
)

data class UsersResponse(
    val success: Boolean,
    val users: List<User>
)