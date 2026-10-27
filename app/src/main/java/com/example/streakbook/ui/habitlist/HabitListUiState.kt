package com.example.streakbook.ui.habitlist

import com.example.streakbook.model.Habit

data class HabitListUiState(
    val habits: List<Habit> = emptyList(),
    val doneCount: Int = 0,
    val error: String? = null
)