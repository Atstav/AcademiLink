package com.example.academilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academilink.viewmodel.MainViewModel

@Composable
fun PostsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val posts by viewModel.posts.observeAsState(emptyList())
    val groups by viewModel.groups.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    var newPostContent by remember { mutableStateOf("") }
    var newPostCategory by remember { mutableStateOf("") }
    var newPostGroupId by remember { mutableStateOf("") }
    val token by viewModel.token.collectAsState(initial = "")

    var editingPostId by remember { mutableStateOf<String?>(null) }
    var editContent by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }

    val currentUser by viewModel.user.observeAsState()

    var searchGroupId by remember { mutableStateOf("") }
    var searchCategory by remember { mutableStateOf("") }
    var searchDateFrom by remember { mutableStateOf("") }
    var searchDateTo by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
        viewModel.loadGroups()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Text("Posts")
        }

        item {
            TextButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        item {
            Text("Search Posts")
        }

        item {
            OutlinedTextField(
                value = searchGroupId,
                onValueChange = { searchGroupId = it },
                label = { Text("Group ID") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = searchCategory,
                onValueChange = { searchCategory = it },
                label = { Text("Category") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = searchDateFrom,
                onValueChange = { searchDateFrom = it },
                label = { Text("Date From (YYYY-MM-DD)") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = searchDateTo,
                onValueChange = { searchDateTo = it },
                label = { Text("Date To (YYYY-MM-DD)") },
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    viewModel.searchPosts(
                        groupId = searchGroupId.trim().trim('"'),
                        category = searchCategory.trim(),
                        dateFrom = searchDateFrom.trim(),
                        dateTo = searchDateTo.trim()
                    )
                }
            ) {
                Text("Search")
            }
        }

        item {
            TextButton(
                onClick = {
                    searchGroupId = ""
                    searchCategory = ""
                    searchDateFrom = ""
                    searchDateTo = ""
                    viewModel.loadPosts()
                }
            ) {
                Text("Show All")
            }
        }

        item {
            Text("Create New Post")
        }

        item {
            OutlinedTextField(
                value = newPostContent,
                onValueChange = { newPostContent = it },
                label = { Text("Content") }
            )
        }

        item {
            OutlinedTextField(
                value = newPostCategory,
                onValueChange = { newPostCategory = it },
                label = { Text("Category") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = newPostGroupId,
                onValueChange = { newPostGroupId = it },
                label = { Text("Group ID") },
                singleLine = true
            )
        }

        item {
            groups.forEach { group ->
                Text("${group.name}: ${group.id}")
            }
        }

        item {
            Button(
                onClick = {
                    if (token.isNotEmpty()) {
                        viewModel.createPost(
                            token = token,
                            content = newPostContent,
                            category = newPostCategory,
                            groupId = newPostGroupId.trim().trim('"')
                        )
                    }
                }
            ) {
                Text("Create Post")
            }
        }

        item {
            if (isLoading) {
                Text("Loading posts...")
            }
        }

        error?.let { message ->
            item {
                Text(message)
            }
        }

        items(posts) { post ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (editingPostId == post.id) {

                        OutlinedTextField(
                            value = editContent,
                            onValueChange = { editContent = it },
                            label = { Text("Content") }
                        )

                        OutlinedTextField(
                            value = editCategory,
                            onValueChange = { editCategory = it },
                            label = { Text("Category") },
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (token.isNotEmpty()) {
                                    viewModel.updatePost(
                                        token = token,
                                        postId = post.id,
                                        content = editContent,
                                        category = editCategory
                                    )

                                    editingPostId = null
                                }
                            }
                        ) {
                            Text("Save Changes")
                        }

                        TextButton(
                            onClick = {
                                editingPostId = null
                            }
                        ) {
                            Text("Cancel")
                        }

                    } else {

                        Text(post.content)
                        Text("Category: ${post.category}")
                        Text("Author: ${post.authorId.username}")
                        Text("Group: ${post.groupId.name}")

                        val isAuthor = post.authorId.id == currentUser?.id

                        if (isAuthor) {
                            Button(
                                onClick = {
                                    editingPostId = post.id
                                    editContent = post.content
                                    editCategory = post.category
                                }
                            ) {
                                Text("Edit")
                            }

                            Button(
                                onClick = {
                                    if (token.isNotEmpty()) {
                                        viewModel.deletePost(
                                            token = token,
                                            postId = post.id
                                        )
                                    }
                                }
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}