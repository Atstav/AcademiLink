package com.example.academilink.network
import com.example.academilink.model.ChatMessage

import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {

    private const val SOCKET_URL = "http://10.0.2.2:3000"

    lateinit var socket: Socket
        private set

    fun connect() {
        if (!::socket.isInitialized) {
            socket = IO.socket(SOCKET_URL)
        }

        if (!socket.connected()) {
            socket.connect()
        }
    }

    fun disconnect() {
        if (::socket.isInitialized && socket.connected()) {
            socket.disconnect()
        }
    }

    fun joinUser(userId: String) {
        if (::socket.isInitialized) {
            socket.emit("join_user", userId)
        }
    }

    fun sendMessage(
        senderId: String,
        receiverId: String,
        content: String
    ) {
        if (!::socket.isInitialized) return

        val message = org.json.JSONObject().apply {
            put("senderId", senderId)
            put("receiverId", receiverId)
            put("content", content)
        }

        socket.emit("send_message", message)
    }

    fun onMessageReceived(
        onMessage: (ChatMessage) -> Unit
    ) {
        if (!::socket.isInitialized) return

        socket.off("receive_message")

        socket.on("receive_message") { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as? org.json.JSONObject ?: return@on

                val message = ChatMessage(
                    senderId = data.optString("senderId"),
                    receiverId = data.optString("receiverId"),
                    content = data.optString("content"),
                    createdAt = data.optString("createdAt")
                )

                onMessage(message)
            }
        }
    }
}