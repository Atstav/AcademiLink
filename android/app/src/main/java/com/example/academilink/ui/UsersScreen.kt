package com.example.academilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academilink.viewmodel.MainViewModel

@Composable
fun UsersScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onChatClick: (String, String) -> Unit
) {
    val users by viewModel.users.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()

    var institution by remember { mutableStateOf("") }
    var fieldOfStudy by remember { mutableStateOf("") }
    var studyYear by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Text("Users")
        }

        item {
            TextButton(
                onClick = onBack
            ) {
                Text("Back")
            }
        }

        item {
            Text("Search Users")
        }

        item {
            OutlinedTextField(
                value = institution,
                onValueChange = { institution = it },
                label = { Text("Institution") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = fieldOfStudy,
                onValueChange = { fieldOfStudy = it },
                label = { Text("Field of Study") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = studyYear,
                onValueChange = { studyYear = it },
                label = { Text("Study Year") },
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    viewModel.searchUsers(
                        institution = institution,
                        fieldOfStudy = fieldOfStudy,
                        studyYear = studyYear
                    )
                }
            ) {
                Text("Search")
            }
        }

        item {
            TextButton(
                onClick = {
                    institution = ""
                    fieldOfStudy = ""
                    studyYear = ""
                    viewModel.loadUsers()
                }
            ) {
                Text("Show All")
            }
        }

        item {
            if (isLoading) {
                Text("Loading users...")
            }
        }

        error?.let { message ->
            item {
                Text(message)
            }
        }

        items(users) { user ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(user.fullName)
                    Text("Username: ${user.username}")
                    Text("Institution: ${user.institution}")
                    Text("Field of Study: ${user.fieldOfStudy}")
                    Text("Study Year: ${user.studyYear}")

                    Button(
                        onClick = {
                            onChatClick(
                                user.id,
                                user.username
                            )
                        }
                    ) {
                        Text("Chat")
                    }
                }
            }
        }
    }
}