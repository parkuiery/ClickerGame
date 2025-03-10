package com.uiel.clickergame

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uiel.clickergame.model.RegisterRequest
import com.uiel.clickergame.network.Retrofit
import com.uiel.clickergame.util.getDeviceUuid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val apiService = Retrofit.apiService
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                apiService.fetchUserData(id = getDeviceUuid().toString())
            }
            .onSuccess { data ->
                _uiState.update {
                    it.copy(
                        nickName = data.nickname ?: "",
                        score = data.score,
                        isNew = false,
                        rank = data.rank,
                    ) }
            }.onFailure {
                _uiState.update { it.copy(isNew = true) }
            }
        }
    }

    fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                apiService.register(
                    registerRequest = RegisterRequest(
                        deviceId = getDeviceUuid().toString(),
                        nickname = uiState.value.nickName
                    )
                )
            }
            .onSuccess {
                _uiState.update { it.copy(isNew = false, isNicknameBad = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(isNicknameBad = true, isNew = true) }
                Log.d("register",error.toString())
            }
        }
    }

    fun updateNickName(nickName: String) {
        _uiState.update { it.copy(nickName = nickName) }
    }

    fun updateScore() {
        val score = _uiState.value.score
        _uiState.update { it.copy(score = score + 1) }
    }

    fun click() {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.click(
                id = getDeviceUuid().toString()
            )
        }
    }

}

data class HomeUiState(
    val nickName: String = "",
    val score: Int = 0,
    val rank: Int = 0,
    val isNew: Boolean = true,
    val isNicknameBad: Boolean = false,
)