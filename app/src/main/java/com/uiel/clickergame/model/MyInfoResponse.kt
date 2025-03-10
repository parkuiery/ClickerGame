package com.uiel.clickergame.model

data class MyInfoResponse(
    val deviceId: String,
    val nickname: String?,
    val score: Int,
    val rank: Int,
)