package com.example.testforwork.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testforwork.domain.entity.IntervalTimer
import com.example.testforwork.domain.usecase.GetIntervalTimerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoadUiState(
    val trainingId: String = "68",
    val isLoading: Boolean = false,
    val error: String? = null,
    val intervalTimer: IntervalTimer? = null
)

class LoadViewModel(
    private val getIntervalTimerUseCase: GetIntervalTimerUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoadUiState())
    val uiState: StateFlow<LoadUiState> = _uiState.asStateFlow()

    fun updateTrainingId(id: String) {
        _uiState.value = _uiState.value.copy(trainingId = id)
    }

    fun loadIntervalTimer(onSuccess: (IntervalTimer) -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getIntervalTimerUseCase(_uiState.value.trainingId)
                .onSuccess { intervalTimer ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        intervalTimer = intervalTimer
                    )
                    onSuccess(intervalTimer)
                }
                .onFailure { exception ->
                    val errorMessage = when {
                        exception.message?.contains("null") == true -> 
                            "Ошибка: некорректный формат данных от сервера"
                        exception.message?.contains("401") == true || 
                        exception.message?.contains("403") == true -> 
                            "Ошибка авторизации. Проверьте токен доступа"
                        exception.message?.contains("404") == true -> 
                            "Тренировка с ID ${_uiState.value.trainingId} не найдена"
                        exception.message?.contains("network") == true || 
                        exception.message?.contains("Unable to resolve") == true -> 
                            "Ошибка сети. Проверьте подключение к интернету"
                        else -> exception.message ?: "Ошибка загрузки тренировки"
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
        }
    }
}

