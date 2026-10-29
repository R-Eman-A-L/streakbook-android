package com.example.streakbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.streakbook.ui.HabitDetailKey
import com.example.streakbook.ui.HabitListKey
import com.example.streakbook.ui.habitdetail.HabitDetailScreen
import com.example.streakbook.ui.habitdetail.HabitDetailViewModel
import com.example.streakbook.ui.habitlist.HabitListScreen
import com.example.streakbook.ui.theme.StreakBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreakBookTheme {
                val backStack = rememberNavBackStack(HabitListKey)
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider {
                        entry<HabitListKey> {
                            HabitListScreen(onOpenHabit = { id -> backStack.add(HabitDetailKey(id)) })
                        }
                        entry<HabitDetailKey> { key ->
                            HabitDetailScreen(
                                viewModel = viewModel { HabitDetailViewModel(habitId = key.habitId) },
                                onBack = { backStack.removeLastOrNull() },
                            )
                        }
                    },
                )
            }
        }
    }
}
