package com.example.testforwork.domain.repository

import com.example.testforwork.domain.entity.IntervalTimer

interface IntervalTimerRepository {
    suspend fun getIntervalTimer(id: String): Result<IntervalTimer>
}

