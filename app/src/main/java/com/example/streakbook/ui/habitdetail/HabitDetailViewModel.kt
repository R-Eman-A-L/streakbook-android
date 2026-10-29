package com.example.streakbook.ui.habitdetail

import androidx.lifecycle.ViewModel
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.InMemoryHabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HabitDetailViewModel(
    private val habitId: Int,
    private val repository: HabitRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HabitDetailUiState())
    val state: StateFlow<HabitDetailUiState> = _state.asStateFlow()


    init {
        load()
    }

    private fun load() {
        val habit = repository.getHabit(habitId)
        if (habit == null) {
            _state.value = HabitDetailUiState(isNotFound = true)
            return
        }
        _state.value = HabitDetailUiState(
            habitName = habit.name,
            doneToday = habit.doneToday,
            isNotFound = false,
        )

    }
}