package com.example.testforwork.presentation.viewmodel

import android.content.Context
import android.media.ToneGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testforwork.core.service.TrainingForegroundService
import com.example.testforwork.domain.entity.IntervalTimer
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TrainingUiState(
    val intervalTimer: IntervalTimer? = null,
    val isRunning: Boolean = false,
    val currentIntervalIndex: Int = 0,
    val remainingTimeInCurrentInterval: Long = 0, // в секундах
    val totalElapsedTime: Long = 0, // в секундах
    val gpsTrack: List<LatLng> = emptyList(),
    val selectedTab: Int = 0 // 0 = Timer, 1 = Map
)

class TrainingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TrainingUiState())
    val uiState: StateFlow<TrainingUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private val toneGenerator = ToneGenerator(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100)

    fun setIntervalTimer(intervalTimer: IntervalTimer) {
        _uiState.value = _uiState.value.copy(
            intervalTimer = intervalTimer,
            currentIntervalIndex = 0,
            remainingTimeInCurrentInterval = intervalTimer.intervals.firstOrNull()?.duration?.toLong() ?: 0L,
            gpsTrack = emptyList(), // Сбрасываем трек для новой тренировки
            totalElapsedTime = 0L,
            isRunning = false
        )
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = tabIndex)
    }

    fun startTraining(context: Context) {
        val timer = _uiState.value.intervalTimer ?: return
        
        _uiState.value = _uiState.value.copy(isRunning = true)
        
        // Запускаем foreground service для работы в фоне
        TrainingForegroundService.startService(context)
        
        // Звуковой сигнал начала тренировки
        playBeep(1)
        
        timerJob = viewModelScope.launch {
            var currentIndex = 0
            var remainingTime = timer.intervals.firstOrNull()?.duration?.toLong() ?: 0L
            var totalElapsed = 0L
            
            while (currentIndex < timer.intervals.size && _uiState.value.isRunning) {
                _uiState.value = _uiState.value.copy(
                    currentIntervalIndex = currentIndex,
                    remainingTimeInCurrentInterval = remainingTime,
                    totalElapsedTime = totalElapsed
                )
                
                if (remainingTime > 0) {
                    delay(1000)
                    remainingTime--
                    totalElapsed++
                } else {
                    // Переход к следующему интервалу
                    currentIndex++
                    if (currentIndex < timer.intervals.size) {
                        // Звуковой сигнал начала интервала
                        playBeep(1)
                        remainingTime = timer.intervals[currentIndex].duration.toLong()
                    } else {
                        // Конец тренировки - два пика
                        playBeep(2)
                        _uiState.value = _uiState.value.copy(isRunning = false)
                        break
                    }
                }
            }
        }
    }

    fun stopTraining(context: Context) {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(isRunning = false)
        TrainingForegroundService.stopService(context)
    }

    fun addGpsPoint(latLng: LatLng) {
        val currentTrack = _uiState.value.gpsTrack.toMutableList()
        currentTrack.add(latLng)
        _uiState.value = _uiState.value.copy(gpsTrack = currentTrack)
    }

    fun resetTrack() {
        _uiState.value = _uiState.value.copy(gpsTrack = emptyList())
    }

    private fun playBeep(count: Int) {
        viewModelScope.launch {
            repeat(count) {
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
                if (count > 1 && it < count - 1) {
                    delay(300)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        toneGenerator.release()
    }
}

