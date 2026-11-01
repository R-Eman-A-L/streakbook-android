package com.example.streakbook.ui.habitdetail

data class HabitDetailUiState(
    val habitName: String? = null,
    val doneToday: Boolean = false,
    val isNotFound: Boolean = false,
    val last7Days: List<Boolean> = emptyList(),
    val currentStreak: Int = 0,
)