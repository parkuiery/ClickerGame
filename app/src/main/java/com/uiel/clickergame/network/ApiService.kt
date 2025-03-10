package com.uiel.clickergame.network

import com.uiel.clickergame.model.MyInfoResponse
import com.uiel.clickergame.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    //사용자 등록 or 닉네임 변경
    @POST("/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    )

    //클릭
    @POST("/click/{id}")
    suspend fun click(
        @Path("id") id: String
    )

    // 사용자 정보 조회
    @GET("/my-info/{id}")
    suspend fun fetchUserData(
        @Path("id") id: String
    ): MyInfoResponse
}