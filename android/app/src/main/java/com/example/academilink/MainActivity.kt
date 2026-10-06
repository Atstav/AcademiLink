package com.example.academilink
import com.example.academilink.viewmodel.MainViewModel
import com.example.academilink.ui.UsersScreen
import com.example.academilink.ui.ChatScreen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.academilink.ui.LoginScreen
import com.example.academilink.ui.RegisterScreen
import com.example.academilink.ui.ProfileScreen
import com.example.academilink.ui.theme.AcademiLinkTheme
import com.example.academilink.ui.GroupsScreen
import com.example.academilink.ui.PostsScreen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AcademiLinkTheme {
                var showRegister by remember { mutableStateOf(false) }
                val user by viewModel.user.observeAsState()
                var showGroups by remember { mutableStateOf(false) }
                var showPosts by remember { mutableStateOf(false) }
                var showUsers by remember { mutableStateOf(false) }
                var chatReceiverId by remember { mutableStateOf<String?>(null) }
                var chatReceiverName by remember { mutableStateOf("") }

                when {
                    user != null && chatReceiverId != null -> {
                        ChatScreen(
                            viewModel = viewModel,
                            currentUserId = user!!.id,
                            receiverId = chatReceiverId!!,
                            receiverName = chatReceiverName,
                            onBack = {
                                chatReceiverId = null
                                chatReceiverName = ""
                            }
                        )
                    }
                    user != null && showGroups -> {
                        GroupsScreen(
                            viewModel = viewModel,
                            onBack = {
                                showGroups = false
                            }
                        )
                    }
                    user != null && showPosts -> {
                        PostsScreen(
                            viewModel = viewModel,
                            onBack = {
                                showPosts = false
                            }
                        )
                    }

                    user != null && showUsers -> {
                        UsersScreen(
                            viewModel = viewModel,
                            onBack = {
                                showUsers = false
                            },
                            onChatClick = { userId, username ->
                                chatReceiverId = userId
                                chatReceiverName = username
                                showUsers = false
                            }
                        )
                    }

                    user != null -> {
                        ProfileScreen(
                            user = user!!,
                            viewModel = viewModel,
                            onLogout = {
                                showRegister = false
                                showGroups = false
                                showPosts = false
                                showUsers = false
                                chatReceiverId = null
                                chatReceiverName = ""
                            },
                            onGroupsClick = {
                                showGroups = true
                            },
                            onPostsClick = {
                                showPosts = true
                            },
                            onUsersClick = {
                                showUsers = true
                            }
                        )
                    }

                    showRegister -> {
                        RegisterScreen(
                            viewModel = viewModel,
                            onBackToLogin = {
                                showRegister = false
                            }
                        )
                    }

                    else -> {
                        LoginScreen(
                            viewModel = viewModel,
                            onRegisterClick = {
                                showRegister = true
                            }
                        )
                    }
                }
            }
        }
        viewModel.groups.observe(this) { groups ->
            Log.d("AcademiLink", "Groups loaded: ${groups.size}")
        }
        viewModel.posts.observe(this) { posts ->
            Log.d("AcademiLink", "Posts loaded: ${posts.size}")
        }
        viewModel.user.observe(this) { user ->
            user?.let {
                Log.d("AcademiLink", "Login successful: ${it.username}")
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Log.e("AcademiLink", it)
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            Log.d("AcademiLink", "Loading: $isLoading")
        }
    }
}