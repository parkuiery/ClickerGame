package com.uiel.clickergame.network

import android.util.Log
import androidx.compose.runtime.MutableState
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.uiel.clickergame.util.SSEUIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.net.URI
import java.util.concurrent.TimeUnit

fun startSSEConnection(
    eventSourceHolder: MutableState<EventSource?>,
    eventHandler: EventHandler,
    coroutineScope: CoroutineScope,
    uiState: MutableState<SSEUIState>
) {
    coroutineScope.launch(Dispatchers.IO) {
        try {
            eventSourceHolder.value =
                EventSource.Builder(
                    eventHandler,
                    URI.create("https://7bc2-2001-e60-a327-1a2e-e471-48a6-d5ff-f4b7.ngrok-free.app/")
                )
                    .reconnectTime(3, TimeUnit.SECONDS)
                    .build()
            eventSourceHolder.value?.start()
        } catch (e: Exception) {
            Log.e("SSE", "Error starting connection: ${e.message}")
            withContext(Dispatchers.Main) {
                uiState.value = uiState.value.copy(
                    messageList = uiState.value.messageList
                )
            }
        }
    }
}
