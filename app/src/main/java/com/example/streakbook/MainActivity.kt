package com.example.streakbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.streakbook.data.SavedHabitRepository
import com.example.streakbook.ui.HabitDetailKey
import com.example.streakbook.ui.HabitListKey
import com.example.streakbook.ui.habitdetail.HabitDetailScreen
import com.example.streakbook.ui.habitdetail.HabitDetailViewModel
import com.example.streakbook.ui.habitlist.HabitListScreen
import com.example.streakbook.ui.theme.StreakBookTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var habitRepository: SavedHabitRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        lifecycleScope.launch {
            habitRepository.load()
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
                                HabitListScreen(
                                    viewModel = hiltViewModel(),
                                    onOpenHabit = { id -> backStack.add(HabitDetailKey(id)) })
                            }
                            entry<HabitDetailKey> { key ->
                                HabitDetailScreen(
                                    viewModel = hiltViewModel<HabitDetailViewModel, HabitDetailViewModel.Factory>(
                                        creationCallback = { factory -> factory.create(key.habitId)}
                                    ),
                                    onBack = { backStack.removeLastOrNull() },
                                )
                            }
                        },
                    )
                }
            }
        }

    }
}
