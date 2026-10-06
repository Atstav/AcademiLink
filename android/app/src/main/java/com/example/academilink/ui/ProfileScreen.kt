package com.example.academilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academilink.model.User
import com.example.academilink.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    user: User,
    viewModel: MainViewModel,
    onLogout: () -> Unit,
    onGroupsClick: () -> Unit,
    onPostsClick: () -> Unit,
    onUsersClick: () -> Unit
) {
    var fullName by remember { mutableStateOf(user.fullName) }
    var institution by remember { mutableStateOf(user.institution) }
    var fieldOfStudy by remember { mutableStateOf(user.fieldOfStudy) }
    var studyYear by remember { mutableStateOf(user.studyYear.toString()) }
    var bio by remember { mutableStateOf(user.bio) }

    val token by viewModel.token.collectAsState(initial = "")
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val updateSuccess by viewModel.profileUpdateSuccess.observeAsState(false)
    var validationError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profile")

        Text("Username: ${user.username}")
        Text("Email: ${user.email}")

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            singleLine = true
        )

        OutlinedTextField(
            value = institution,
            onValueChange = { institution = it },
            label = { Text("Institution") },
            singleLine = true
        )

        OutlinedTextField(
            value = fieldOfStudy,
            onValueChange = { fieldOfStudy = it },
            label = { Text("Field of Study") },
            singleLine = true
        )

        OutlinedTextField(
            value = studyYear,
            onValueChange = { studyYear = it },
            label = { Text("Study Year") },
            singleLine = true
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") }
        )

        if (isLoading) {
            Text("Saving...")
        }

        error?.let {
            Text(it)
        }

        validationError?.let {
            Text(it)
        }

        if (updateSuccess) {
            Text("Profile updated successfully!")
        }

        Button(
            onClick = {
                viewModel.clearProfileUpdateSuccess()
                val year = studyYear.toIntOrNull()

                validationError = when {
                    fullName.isBlank() -> "Full Name is required"
                    institution.isBlank() -> "Institution is required"
                    fieldOfStudy.isBlank() -> "Field of Study is required"
                    year == null -> "Study Year must be a number"
                    year !in 1..7 -> "Study Year must be between 1 and 7"
                    token.isEmpty() -> "Authentication token is missing"
                    else -> null
                }

                if (validationError == null) {
                    viewModel.updateProfile(
                        token = token,
                        fullName = fullName,
                        institution = institution,
                        fieldOfStudy = fieldOfStudy,
                        studyYear = year!!,
                        bio = bio
                    )
                }
            }
        ) {
            Text("Save Profile")
        }

        Button(
            onClick = onGroupsClick
        ) {
            Text("Study Groups")
        }

        Button(
            onClick = onPostsClick
        ) {
            Text("Posts")
        }

        Button(
            onClick = onUsersClick
        ) {
            Text("Users")
        }

        Button(
            onClick = {
                if (token.isNotEmpty()) {
                    viewModel.deleteProfile(token)
                    onLogout()
                }
            }
        ) {
            Text("Delete Account")
        }
        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            }
        ) {
            Text("Logout")
        }
    }
}