package com.example.streakbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.streakbook.ui.HabitDetailKey
import com.example.streakbook.ui.HabitListKey
import com.example.streakbook.ui.habitdetail.HabitDetailScreen
import com.example.streakbook.ui.habitlist.HabitListScreen
import com.example.streakbook.ui.habitlist.HabitListViewModel
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
                    onBack = {backStack.removeLastOrNull()},
                    entryProvider = entryProvider {
                        entry<HabitListKey> {
                            HabitListScreen(onOpenHabit = { id -> backStack.add(HabitDetailKey(id)) })
                        }
                        entry<HabitDetailKey> { key ->
                            HabitDetailScreen(habitId = key.habitId, onBack = {
                                backStack.removeLastOrNull()
                            })
                        }
                    },
                )
            }
        }
    }
}


