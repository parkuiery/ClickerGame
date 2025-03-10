package com.uiel.clickergame.model

import kotlinx.serialization.Serializable

@Serializable
data class RankingDto(
    val deviceId: String = "",
    val nickname: String = "",
    val score: Int = 0,
)