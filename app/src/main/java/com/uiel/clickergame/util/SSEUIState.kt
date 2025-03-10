package com.uiel.clickergame.util

data class SSEUIState(
    val messageList: List<SSEMessage> = emptyList(),
    val isConnected: Boolean = false
)
