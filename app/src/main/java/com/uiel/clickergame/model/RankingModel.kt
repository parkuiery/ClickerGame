package com.uiel.clickergame.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RankingModel(
    val deviceId: String = "",
    val nickname: String = "",
    val score: Int = 0,
)