package com.example.testforwork.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IntervalTimer(
    val id: String,
    val intervals: List<Interval>
) : Parcelable

@Parcelize
data class Interval(
    val duration: Int, // в секундах
    val type: String? = null
) : Parcelable

