package com.example.testforwork.data.repository

import com.example.testforwork.data.datasource.RemoteIntervalTimerDataSource
import com.example.testforwork.domain.entity.IntervalTimer
import com.example.testforwork.domain.repository.IntervalTimerRepository

class IntervalTimerRepositoryImpl(
    private val remoteDataSource: RemoteIntervalTimerDataSource
) : IntervalTimerRepository {
    override suspend fun getIntervalTimer(id: String): Result<IntervalTimer> {
        return remoteDataSource.getIntervalTimer(id)
    }
}

