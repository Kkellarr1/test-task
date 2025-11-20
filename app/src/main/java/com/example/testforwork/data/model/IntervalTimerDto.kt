package com.example.testforwork.data.model

import com.example.testforwork.domain.entity.Interval
import com.example.testforwork.domain.entity.IntervalTimer
import com.google.gson.annotations.SerializedName

// Обертка для ответа API
data class TimerResponseDto(
    @SerializedName("timer")
    val timer: IntervalTimerDto? = null
) {
    fun toDomain(): IntervalTimer? {
        return timer?.toDomain()
    }
}

data class IntervalTimerDto(
    @SerializedName("timer_id")
    val timerId: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("total_time")
    val totalTime: Int? = null,
    @SerializedName("intervals")
    val intervals: List<IntervalDto>? = null
) {
    fun toDomain(): IntervalTimer {
        return IntervalTimer(
            id = timerId?.toString() ?: "",
            intervals = intervals?.mapNotNull { it.toDomain() } ?: emptyList()
        )
    }
}

data class IntervalDto(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("time")
    val time: Int? = null
) {
    fun toDomain(): Interval? {
        return time?.let {
            Interval(
                duration = it,
                type = title
            )
        }
    }
}

