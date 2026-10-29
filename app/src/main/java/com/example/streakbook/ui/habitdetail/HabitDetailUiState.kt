package com.example.streakbook.ui.habitdetail

data class HabitDetailUiState(
    val habitName: String? = null,
    val doneToday: Boolean = false,
    val isNotFound: Boolean = false,
)