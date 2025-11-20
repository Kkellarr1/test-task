package com.example.testforwork.domain.usecase

import com.example.testforwork.domain.entity.IntervalTimer
import com.example.testforwork.domain.repository.IntervalTimerRepository

class GetIntervalTimerUseCase(
    private val repository: IntervalTimerRepository
) {
    suspend operator fun invoke(id: String): Result<IntervalTimer> {
        return repository.getIntervalTimer(id)
    }
}

