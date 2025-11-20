package com.example.testforwork.data.datasource

import com.example.testforwork.data.api.ApiClient
import com.example.testforwork.domain.entity.IntervalTimer
import retrofit2.HttpException

interface RemoteIntervalTimerDataSource {
    suspend fun getIntervalTimer(id: String): Result<IntervalTimer>
}

class RemoteIntervalTimerDataSourceImpl : RemoteIntervalTimerDataSource {
    override suspend fun getIntervalTimer(id: String): Result<IntervalTimer> {
        return try {
            val response = ApiClient.intervalTimerApi.getIntervalTimer(id)
            
            // Проверка на null значения
            if (response.timer == null) {
                return Result.failure(IllegalStateException("Данные тренировки отсутствуют в ответе"))
            }
            
            val domain = response.toDomain()
            
            // Проверка на null после маппинга
            if (domain == null) {
                return Result.failure(IllegalStateException("Не удалось преобразовать данные тренировки"))
            }
            
            // Валидация: проверяем, что есть хотя бы один интервал
            if (domain.intervals.isEmpty()) {
                Result.failure(IllegalStateException("Тренировка не содержит интервалов"))
            } else {
                Result.success(domain)
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Ошибка авторизации"
                403 -> "Доступ запрещен"
                404 -> "Тренировка не найдена"
                500 -> "Ошибка сервера"
                else -> "HTTP ошибка: ${e.code()}"
            }
            Result.failure(Exception(errorMessage, e))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

