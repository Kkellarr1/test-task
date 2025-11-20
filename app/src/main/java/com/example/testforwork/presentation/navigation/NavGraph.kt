package com.example.testforwork.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testforwork.domain.entity.IntervalTimer
import com.example.testforwork.presentation.screen.LoadScreen
import com.example.testforwork.presentation.screen.TrainingScreen

sealed class Screen(val route: String) {
    object Load : Screen("load")
    object Training : Screen("training")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Load.route
    ) {
        composable(Screen.Load.route) {
            LoadScreen(
                onNavigateToTraining = { intervalTimer ->
                    // Сохраняем данные в savedStateHandle перед навигацией
                    // Сохраняем в текущий entry, который будет доступен как previousBackStackEntry
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "intervalTimer",
                        intervalTimer
                    )
                    
                    navController.navigate(Screen.Training.route) {
                        popUpTo(Screen.Load.route) { 
                            inclusive = false
                        }
                    }
                }
            )
        }
        
        composable(Screen.Training.route) { backStackEntry ->
            // Получаем данные из savedStateHandle с использованием state
            var intervalTimer by remember { 
                mutableStateOf<IntervalTimer?>(
                    backStackEntry.savedStateHandle.get<IntervalTimer>("intervalTimer")
                )
            }
            
            // Пытаемся получить данные из savedStateHandle при создании экрана
            // Также проверяем previousBackStackEntry на случай, если данные там
            LaunchedEffect(backStackEntry) {
                var timer = backStackEntry.savedStateHandle.get<IntervalTimer>("intervalTimer")
                if (timer == null) {
                    // Пытаемся получить из previousBackStackEntry
                    timer = navController.previousBackStackEntry?.savedStateHandle?.get<IntervalTimer>("intervalTimer")
                }
                if (timer != null && intervalTimer == null) {
                    intervalTimer = timer
                }
            }
            
            if (intervalTimer != null) {
                TrainingScreen(intervalTimer = intervalTimer!!)
            } else {
                // Fallback UI если данные не переданы - показываем ошибку
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Ошибка загрузки данных",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Попробуйте загрузить тренировку снова",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

