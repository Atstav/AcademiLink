package com.example.academilink.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.academilink.viewmodel.MainViewModel

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onRegisterClick: () -> Unit
) {
    val savedEmail by viewModel.lastEmail.collectAsState(initial = "")
    var email by remember(savedEmail) { mutableStateOf(savedEmail) }
    var password by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val user by viewModel.user.observeAsState()

    fun submitLogin() {
        val cleanEmail = email
            .trim()
            .lowercase()
            .replace(Regex("\\s"), "")
            .replace(
                Regex("[\\u200B-\\u200F\\u202A-\\u202E\\u2060-\\u206F]"),
                ""
            )

        validationError = when {
            cleanEmail.isBlank() -> "Email is required"

            !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() ->
                "Enter a valid email"

            password.isBlank() -> "Password is required"

            else -> null
        }

        if (validationError == null) {
            email = cleanEmail
            viewModel.saveLastEmail(cleanEmail)
            viewModel.login(cleanEmail, password)
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            viewModel.saveLastEmail(email)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("AcademiLink")

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                validationError = null
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
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
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    submitLogin()
                }
            ),
            modifier = Modifier.onPreviewKeyEvent { event ->
                if (event.key == Key.Enter) {
                    submitLogin()
                    true
                } else {
                    false
                }
            }
        )

        Button(
            onClick = {
                submitLogin()
            }
        ) {
            Text("Login")
        }

        TextButton(
            onClick = onRegisterClick
        ) {
            Text("Create Account")
        }

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
            Text("Welcome ${it.username}")
        }
    }
}