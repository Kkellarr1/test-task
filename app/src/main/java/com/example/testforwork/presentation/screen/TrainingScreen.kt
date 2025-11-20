package com.example.testforwork.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testforwork.domain.entity.IntervalTimer
import com.example.testforwork.presentation.viewmodel.TrainingViewModel
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
    intervalTimer: IntervalTimer,
    viewModel: TrainingViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(intervalTimer) {
        viewModel.setIntervalTimer(intervalTimer)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тренировка") }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (uiState.isRunning) {
                        viewModel.stopTraining(context)
                    } else {
                        viewModel.startTraining(context)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (uiState.isRunning) "Стоп" else "Старт")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Вкладки
            TabRow(selectedTabIndex = uiState.selectedTab) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = { Text("Таймер") }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = { Text("Карта") }
                )
            }

            // Контент вкладок
            when (uiState.selectedTab) {
                0 -> TimerTabContent(uiState = uiState, intervalTimer = intervalTimer)
                1 -> MapTabContent(
                    uiState = uiState,
                    isRunning = uiState.isRunning,
                    onGpsPointAdded = { viewModel.addGpsPoint(it) }
                )
            }
        }
    }
}

@Composable
fun TimerTabContent(
    uiState: com.example.testforwork.presentation.viewmodel.TrainingUiState,
    intervalTimer: IntervalTimer
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Общая длительность
        val totalDuration = intervalTimer.intervals.sumOf { it.duration }
        val formattedTotalTime = formatTime(totalDuration.toLong())
        val formattedElapsedTime = formatTime(uiState.totalElapsedTime)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Общая длительность",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formattedTotalTime,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Прошло времени",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = formattedElapsedTime,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Текущий интервал
        if (uiState.currentIntervalIndex < intervalTimer.intervals.size) {
            val currentInterval = intervalTimer.intervals[uiState.currentIntervalIndex]
            val formattedRemaining = formatTime(uiState.remainingTimeInCurrentInterval)
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isRunning) 
                        MaterialTheme.colorScheme.secondaryContainer 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Интервал ${uiState.currentIntervalIndex + 1}/${intervalTimer.intervals.size}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = formattedRemaining,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Длительность: ${formatTime(currentInterval.duration.toLong())}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Список всех интервалов
        Text(
            text = "Интервалы",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        intervalTimer.intervals.forEachIndexed { index, interval ->
            IntervalBar(
                interval = interval,
                index = index,
                isActive = index == uiState.currentIntervalIndex && uiState.isRunning,
                isCompleted = index < uiState.currentIntervalIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun IntervalBar(
    interval: com.example.testforwork.domain.entity.Interval,
    index: Int,
    isActive: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = when {
                isActive -> MaterialTheme.colorScheme.primary
                isCompleted -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Интервал ${index + 1}",
                style = MaterialTheme.typography.bodyLarge,
                color = if (isActive || isCompleted) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatTime(interval.duration.toLong()),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isActive || isCompleted) 
                    MaterialTheme.colorScheme.onPrimary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MapTabContent(
    uiState: com.example.testforwork.presentation.viewmodel.TrainingUiState,
    isRunning: Boolean,
    onGpsPointAdded: (LatLng) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Карта будет здесь
        MapView(
            track = uiState.gpsTrack,
            isTracking = isRunning,
            onGpsPointAdded = onGpsPointAdded
        )
    }
}

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%d:%02d", minutes, secs)
    }
}

