package com.example.testforwork.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testforwork.domain.entity.IntervalTimer
import com.example.testforwork.domain.usecase.GetIntervalTimerUseCase
import com.example.testforwork.presentation.viewmodel.LoadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadScreen(
    onNavigateToTraining: (IntervalTimer) -> Unit,
    getIntervalTimerUseCase: GetIntervalTimerUseCase,
    viewModel: LoadViewModel = viewModel { LoadViewModel(getIntervalTimerUseCase) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var trainingIdText by remember { mutableStateOf(uiState.trainingId) }

    LaunchedEffect(uiState.trainingId) {
        trainingIdText = uiState.trainingId
    }

    LaunchedEffect(uiState.intervalTimer) {
        uiState.intervalTimer?.let {
            onNavigateToTraining(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Загрузка тренировки") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = trainingIdText,
                onValueChange = { 
                    trainingIdText = it
                    viewModel.updateTrainingId(it)
                },
                label = { Text("ID тренировки") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.loadIntervalTimer(onNavigateToTraining)
                },
                enabled = !uiState.isLoading && trainingIdText.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Загрузка...")
                } else {
                    Text("Загрузить")
                }
            }

            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

