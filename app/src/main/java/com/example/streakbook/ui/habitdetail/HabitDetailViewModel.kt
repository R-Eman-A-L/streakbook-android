package com.example.streakbook.ui.habitdetail

import androidx.lifecycle.ViewModel
import com.example.streakbook.data.Clock
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.InMemoryHabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HabitDetailViewModel(
    private val habitId: Int,
    private val repository: HabitRepository,
    private val clock: Clock
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
        val doneToday = clock.today() in habit.completedDates
        _state.value = HabitDetailUiState(
            habitName = habit.name,
            doneToday = doneToday,
            isNotFound = false,
        )
    }
}