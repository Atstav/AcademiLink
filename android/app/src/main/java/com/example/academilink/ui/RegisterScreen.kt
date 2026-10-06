package com.example.academilink.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.academilink.viewmodel.MainViewModel

@Composable
fun RegisterScreen(
    viewModel: MainViewModel,
    onBackToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var institution by remember { mutableStateOf("") }
    var fieldOfStudy by remember { mutableStateOf("") }
    var studyYear by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val user by viewModel.user.observeAsState()

    fun submitRegistration() {
        val year = studyYear.toIntOrNull()

        validationError = when {
            username.isBlank() -> "Username is required"
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Enter a valid email"
            fullName.isBlank() -> "Full Name is required"
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            institution.isBlank() -> "Institution is required"
            fieldOfStudy.isBlank() -> "Field of Study is required"
            year == null -> "Study Year must be a number"
            year !in 1..7 -> "Study Year must be between 1 and 7"
            else -> null
        }

        if (validationError == null) {
            viewModel.register(
                username = username,
                email = email,
                password = password,
                fullName = fullName,
                institution = institution,
                fieldOfStudy = fieldOfStudy,
                studyYear = year!!,
                bio = bio
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account")

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                validationError = null
            },
            label = { Text("Username") },
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                validationError = null
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                validationError = null
            },
            label = { Text("Full Name") },
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                validationError = null
            },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        OutlinedTextField(
            value = institution,
            onValueChange = {
                institution = it
                validationError = null
            },
            label = { Text("Institution") },
            singleLine = true
        )

        OutlinedTextField(
            value = fieldOfStudy,
            onValueChange = {
                fieldOfStudy = it
                validationError = null
            },
            label = { Text("Field of Study") },
            singleLine = true
        )

        OutlinedTextField(
            value = studyYear,
            onValueChange = {
                studyYear = it
                validationError = null
            },
            label = { Text("Study Year") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true
        )

        OutlinedTextField(
            value = bio,
            onValueChange = {
                bio = it
                validationError = null
            },
            label = { Text("Bio") }
        )

        if (isLoading) {
            Text("Loading...")
        }

        error?.let {
            Text(it)
        }

        validationError?.let {
            Text(it)
        }

        user?.let {
            Text("Registration successful!")
        }

        Button(
            onClick = {
                submitRegistration()
            }
        ) {
            Text("Register")
        }

        TextButton(
            onClick = onBackToLogin
        ) {
            Text("Back to Login")
        }
    }
}