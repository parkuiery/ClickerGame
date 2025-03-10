package com.uiel.clickergame.util

sealed class SSEMessage {
    data class Connected(val message: String = "연결됨") : SSEMessage()
    data class Comment(val message: String) : SSEMessage()
    data class Disconnected(val message: String = "연결 종료") : SSEMessage()
    data class Error(val message: String) : SSEMessage()
}
