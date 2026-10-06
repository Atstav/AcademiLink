package com.example.academilink.viewmodel
import com.example.academilink.data.local.UserPreferences
import com.example.academilink.model.Post
import com.example.academilink.model.StudyGroup
import com.example.academilink.model.User
import com.example.academilink.repository.AcademiLinkRepository
import com.example.academilink.model.ChatMessage
import com.example.academilink.network.SocketManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AcademiLinkRepository()
    private val userPreferences = UserPreferences(application)
    val lastEmail = userPreferences.lastEmail
    val token = userPreferences.token
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _profileUpdateSuccess = MutableLiveData(false)
    val profileUpdateSuccess: LiveData<Boolean> = _profileUpdateSuccess

    private val _groups = MutableLiveData<List<StudyGroup>>()
    val groups: LiveData<List<StudyGroup>> = _groups

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    fun saveLastEmail(email: String) {
        viewModelScope.launch {
            userPreferences.saveLastEmail(email)
        }
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _user.value = null
            _error.value = null
            _isLoading.value = true

            try {
                val response = repository.login(email, password)

                if (response.isSuccessful) {
                    val body = response.body()

                    _user.value = body?.user

                    body?.token?.let { token ->
                        userPreferences.saveToken(token)
                    }
                } else {
                    _error.value = "Login failed"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun register(
        username: String,
        email: String,
        password: String,
        fullName: String,
        institution: String,
        fieldOfStudy: String,
        studyYear: Int,
        bio: String = ""
    ) {
        viewModelScope.launch {
            _user.value = null
            _error.value = null
            _isLoading.value = true

            try {
                val response = repository.register(
                    username = username,
                    email = email,
                    password = password,
                    fullName = fullName,
                    institution = institution,
                    fieldOfStudy = fieldOfStudy,
                    studyYear = studyYear,
                    bio = bio
                )

                if (response.isSuccessful) {
                    val body = response.body()

                    _user.value = body?.user

                    body?.token?.let { token ->
                        userPreferences.saveToken(token)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()

                    _error.value =
                        "Registration failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val response = repository.getGroups()

                if (response.isSuccessful) {
                    _groups.value = response.body()?.groups.orEmpty()
                    _error.value = null
                } else {
                    _error.value = "Failed to load groups"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            val response = repository.getPosts()

            if (response.isSuccessful) {
                _posts.value = response.body()?.posts.orEmpty()
            } else {
                _error.value = "Failed to load posts"
            }
        }
    }

    fun createPost(
        token: String,
        content: String,
        category: String,
        groupId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.createPost(
                    token = token,
                    content = content,
                    category = category,
                    groupId = groupId
                )

                if (response.isSuccessful) {
                    loadPosts()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Create post failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePost(
        token: String,
        postId: String,
        content: String,
        category: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.updatePost(
                    token = token,
                    postId = postId,
                    content = content,
                    category = category
                )

                if (response.isSuccessful) {
                    loadPosts()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Update post failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchPosts(
        groupId: String,
        category: String,
        dateFrom: String,
        dateTo: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.searchPosts(
                    groupId = groupId,
                    category = category,
                    dateFrom = dateFrom,
                    dateTo = dateTo
                )

                if (response.isSuccessful) {
                    _posts.value = response.body()?.posts.orEmpty()
                } else {
                    _error.value = "Failed to search posts"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletePost(
        token: String,
        postId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.deletePost(
                    token = token,
                    postId = postId
                )

                if (response.isSuccessful) {
                    loadPosts()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Delete post failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(
        token: String,
        fullName: String,
        institution: String,
        fieldOfStudy: String,
        studyYear: Int,
        bio: String
    ) {
        viewModelScope.launch {
            _error.value = null
            _isLoading.value = true
            _profileUpdateSuccess.value = false

            try {
                val response = repository.updateProfile(
                    token = token,
                    fullName = fullName,
                    institution = institution,
                    fieldOfStudy = fieldOfStudy,
                    studyYear = studyYear,
                    bio = bio
                )

                if (response.isSuccessful) {
                    _user.value = response.body()?.user
                    _profileUpdateSuccess.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Update failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteProfile(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.deleteProfile(token)

                if (response.isSuccessful) {
                    _user.value = null
                    userPreferences.saveToken("")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Delete profile failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearProfileUpdateSuccess() {
        _profileUpdateSuccess.value = false
    }

    fun logout() {
        _user.value = null
        _profileUpdateSuccess.value = false
        _error.value = null
    }

    fun searchGroups(
        institution: String,
        course: String,
        category: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.searchGroups(
                    institution = institution,
                    course = course,
                    category = category
                )

                if (response.isSuccessful) {
                    _groups.value = response.body()?.groups.orEmpty()
                } else {
                    _error.value = "Failed to search groups"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun createGroup(
        token: String,
        name: String,
        description: String,
        institution: String,
        course: String,
        category: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.createGroup(
                    token = token,
                    name = name,
                    description = description,
                    institution = institution,
                    course = course,
                    category = category
                )

                if (response.isSuccessful) {
                    loadGroups()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Create group failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteGroup(
        token: String,
        groupId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.deleteGroup(
                    token = token,
                    groupId = groupId
                )

                if (response.isSuccessful) {
                    loadGroups()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Delete group failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateGroup(
        token: String,
        groupId: String,
        name: String,
        description: String,
        institution: String,
        course: String,
        category: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.updateGroup(
                    token = token,
                    groupId = groupId,
                    name = name,
                    description = description,
                    institution = institution,
                    course = course,
                    category = category
                )

                if (response.isSuccessful) {
                    loadGroups()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Update group failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun joinGroup(
        token: String,
        groupId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.joinGroup(
                    token = token,
                    groupId = groupId
                )

                if (response.isSuccessful) {
                    loadGroups()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Join group failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun leaveGroup(
        token: String,
        groupId: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.leaveGroup(
                    token = token,
                    groupId = groupId
                )

                if (response.isSuccessful) {
                    loadGroups()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value =
                        "Leave group failed (${response.code()}): $errorBody"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.getUsers()

                if (response.isSuccessful) {
                    _users.value = response.body()?.users.orEmpty()
                } else {
                    _error.value = "Failed to load users"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchUsers(
        institution: String,
        fieldOfStudy: String,
        studyYear: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = repository.searchUsers(
                    institution = institution,
                    fieldOfStudy = fieldOfStudy,
                    studyYear = studyYear
                )

                if (response.isSuccessful) {
                    _users.value = response.body()?.users.orEmpty()
                } else {
                    _error.value = "Failed to search users"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun connectChat(userId: String) {
        SocketManager.connect()
        SocketManager.joinUser(userId)

        SocketManager.onMessageReceived { message ->
            val currentMessages = _messages.value.orEmpty()
            _messages.postValue(currentMessages + message)
        }
    }

    fun sendChatMessage(
        senderId: String,
        receiverId: String,
        content: String
    ) {
        if (content.isBlank()) return

        val message = ChatMessage(
            senderId = senderId,
            receiverId = receiverId,
            content = content
        )

        val currentMessages = _messages.value.orEmpty()
        _messages.value = currentMessages + message

        SocketManager.sendMessage(
            senderId = senderId,
            receiverId = receiverId,
            content = content
        )
    }

    fun clearChat() {
        _messages.value = emptyList()
    }
}
