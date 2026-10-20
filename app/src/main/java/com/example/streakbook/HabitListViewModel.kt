package com.example.streakbook

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HabitListViewModel(
    private val repository: HabitRepository = InMemoryHabitRepository()
) : ViewModel() {

    private val _habits = MutableStateFlow(repository.getHabits())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()


    fun toggle(id: Int) {
        repository.toggle(id)
        _habits.value = repository.getHabits()
    }

    fun validate(value: String): String? {
        val name = value.trim()
        return when {
            name.isBlank() -> "Enter a habit name"
            repository.getHabits().any { it.name == name } -> "You already have that habit"
            else -> null
        }
    }

    fun deleteHabit(id: Int) {
        repository.delete(id)
        _habits.value = repository.getHabits()
    }

    fun addHabit(name: String) {
        if (validate(name) != null) return
        repository.add(name)
        _habits.value = repository.getHabits()
    }
}