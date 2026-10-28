package com.example.streakbook.ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HabitListKey : NavKey
@Serializable
data class HabitDetailKey(val habitId: Int) : NavKey