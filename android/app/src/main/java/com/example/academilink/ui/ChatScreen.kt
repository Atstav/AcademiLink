package com.example.academilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
fun ChatScreen(
    viewModel: MainViewModel,
    currentUserId: String,
    receiverId: String,
    receiverName: String,
    onBack: () -> Unit
) {
    val messages by viewModel.messages.observeAsState(emptyList())

    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(currentUserId) {
        viewModel.connectChat(currentUserId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Chat with $receiverName")

        TextButton(
            onClick = {
                viewModel.clearChat()
                onBack()
            }
        ) {
            Text("Back")
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                val belongsToConversation =
                    (message.senderId == currentUserId &&
                            message.receiverId == receiverId) ||
                            (message.senderId == receiverId &&
                                    message.receiverId == currentUserId)

                if (belongsToConversation) {
                    val senderLabel =
                        if (message.senderId == currentUserId) "Me"
                        else receiverName

                    Text("$senderLabel: ${message.content}")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Message") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendChatMessage(
                            senderId = currentUserId,
                            receiverId = receiverId,
                            content = messageText
                        )

                        messageText = ""
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}