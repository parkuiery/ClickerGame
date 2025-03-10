package com.uiel.clickergame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.launchdarkly.eventsource.EventHandler
import com.launchdarkly.eventsource.EventSource
import com.launchdarkly.eventsource.MessageEvent
import com.uiel.clickergame.model.RankingDto
import com.uiel.clickergame.model.RankingModel
import com.uiel.clickergame.network.Retrofit
import com.uiel.clickergame.util.SSEMessage
import com.uiel.clickergame.util.SSEUIState
import com.uiel.clickergame.util.getDeviceUuid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URI
import java.util.UUID
import java.util.concurrent.TimeUnit

class RankingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    private val apiService = Retrofit.apiService

    private var eventSourceHolder: EventSource? = null

    fun fetchUserData() {
        viewModelScope.launch {
            kotlin.runCatching {
                apiService.fetchUserData(
                    id = getDeviceUuid().toString()
                )
            }.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        myRank = data.rank,
                        myNickname = data.nickname ?: "",
                        myScore = data.score,
                    )
                }
            }.onFailure {
                Log.d("ranking-fetchUserData",it.message.toString())
            }
        }
    }

    fun startSSEConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventSourceHolder = EventSource.Builder(
                    createEventHandler(),
                    URI.create("https://dsm-freshman-server.xquare.app/sse")
                )
                    .reconnectTime(3, TimeUnit.SECONDS)
                    .build()
                eventSourceHolder?.start()
            } catch (e: Exception) {
                Log.e("SSE", "Error starting connection: ${e.message}")
            }
        }
    }

    private fun createEventHandler(): EventHandler = object : EventHandler {
        override fun onOpen() {
            viewModelScope.launch {
                Log.d("SSE", "eventHandler onOpen")
                Log.d("SSE", "Connected to: ${eventSourceHolder?.uri}")
                _uiState.update {
                    it.copy(
                        isConnected = true,
                        rankingList = uiState.value.rankingList,
                    )
                }
            }
        }

        override fun onClosed() {
            viewModelScope.launch {
                Log.d("SSE", "eventHandler onClosed")
                _uiState.update {
                    it.copy(
                        isConnected = false,
                        rankingList = uiState.value.rankingList,
                    )
                }
            }
        }

        override fun onMessage(event: String, messageEvent: MessageEvent) {
            viewModelScope.launch {
                try {
                    Log.d("sse", messageEvent.data)
                    val rankingList = Json.decodeFromString<List<RankingModel>>(messageEvent.data)
                    _uiState.update { it.copy(rankingList = rankingList) }
                } catch (e: Exception) {
                    Log.d("ranking", e.message.toString())
                    _uiState.update {
                        it.copy(
                            rankingList = uiState.value.rankingList,
                        )
                    }
                }
            }
        }

        override fun onComment(comment: String) {
            viewModelScope.launch {
                Log.d("SSE", "eventHandler onComment : $comment")
                _uiState.update {
                    it.copy(
                        rankingList = uiState.value.rankingList,
                    )
                }
            }
        }

        override fun onError(t: Throwable) {
            viewModelScope.launch {
                Log.d("SSE", "eventHandler onError : $t")
                _uiState.update {
                    it.copy(
                        isConnected = false,
                        rankingList = uiState.value.rankingList,
                    )
                }
            }
        }
    }

}

data class RankingUiState(
    val isConnected: Boolean = false,
    val rankingList: List<RankingModel> = emptyList(),
    val id: String = UUID.randomUUID().toString(),
    val myRank: Int? = null,
    val myNickname: String = "",
    val myScore: Int = 0,
)