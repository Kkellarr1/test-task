package com.example.testforwork.di

import com.example.testforwork.data.datasource.RemoteIntervalTimerDataSource
import com.example.testforwork.data.datasource.RemoteIntervalTimerDataSourceImpl
import com.example.testforwork.data.repository.IntervalTimerRepositoryImpl
import com.example.testforwork.domain.repository.IntervalTimerRepository
import com.example.testforwork.domain.usecase.GetIntervalTimerUseCase

object AppModule {
    // Data Source
    private val remoteDataSource: RemoteIntervalTimerDataSource by lazy {
        RemoteIntervalTimerDataSourceImpl()
    }

    // Repository
    val intervalTimerRepository: IntervalTimerRepository by lazy {
        IntervalTimerRepositoryImpl(remoteDataSource)
    }

    // Use Cases
    val getIntervalTimerUseCase: GetIntervalTimerUseCase by lazy {
        GetIntervalTimerUseCase(intervalTimerRepository)
    }
}

