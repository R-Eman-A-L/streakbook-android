package com.example.streakbook.ui.habitlist

import androidx.lifecycle.ViewModel
import com.example.streakbook.data.Clock
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.InMemoryHabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HabitListViewModel(
    private val repository: HabitRepository,
    private val clock: Clock
) : ViewModel() {

    private val _state = MutableStateFlow(HabitListUiState())
    val state: StateFlow<HabitListUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    private fun refresh(error: String? = null) {
        val habits = repository.getHabits()
        val today = clock.today()
//        val doneTodayIds: MutableSet<Int> = mutableSetOf()
//        habits.forEach {
//            if (today in it.completedDates) doneTodayIds += it.id
//        }

        val doneTodayIds = habits.filter { today in it.completedDates }.map { it.id }.toSet()

        _state.value = HabitListUiState(
            habits = habits,
            doneTodayIds = doneTodayIds,
            doneCount = doneTodayIds.size,
            error = error
        )
    }

    fun onToggle(id: Int) {
        val today = clock.today()
        repository.toggle(id, today)
        refresh()
    }

    fun onDelete(id: Int) {
        repository.delete(id)
        refresh()
    }

    fun onAdd(name: String): Boolean {
        val problem = validate(name)
        if (problem != null) {
            refresh(problem)
            return false
        }
        repository.add(name)
        refresh()
        return true
    }

    fun onTextChanged() {
        _state.value = _state.value.copy(error = null)
    }

    private fun validate(value: String): String? {
        val name = value.trim()
        return when {
            name.isBlank() -> "Enter a habit name"
            repository.getHabits().any { it.name == name } -> "You already have that habit"
            else -> null
        }
    }

}