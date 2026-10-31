package com.example.streakbook.ui.habitlist

import com.example.streakbook.model.Habit

data class HabitListUiState(
    val habits: List<Habit> = emptyList(),
    val doneCount: Int = 0,
    val doneTodayIds: Set<Int> = emptySet(),
    val error: String? = null
)