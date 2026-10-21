package com.example.streakbook

data class HabitListUiState(
    val habits: List<Habit> = emptyList(),
    val doneCount: Int = 0,
    val error: String? = null
)