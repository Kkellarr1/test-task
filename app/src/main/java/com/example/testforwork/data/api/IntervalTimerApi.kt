package com.example.testforwork.data.api

import com.example.testforwork.data.model.TimerResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface IntervalTimerApi {
    @GET("api/v2/interval-timers/{id}")
    suspend fun getIntervalTimer(
        @Path("id") id: String
    ): TimerResponseDto
}

