package com.example.streakbook

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HabitListViewModel : ViewModel() {
    private val _habits = MutableStateFlow(
        listOf(
            Habit(1, "Read"),
            Habit(2, "Gym"),
            Habit(3, "Water"),
            Habit(4, "Walk")
        )
    )

    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()
    private var nextId = 5

    fun toggle(id: Int) {
        _habits.update { list ->
            list.map {
                if (it.id == id)
                    it.copy(doneToday = !it.doneToday) else it
            }
        }
    }

    fun validate(value: String): String? {
        val name = value.trim()
        return when {
            name.isBlank() -> "Enter a habit name"
            _habits.value.any { it.name == name } -> "You already have that habit"
            else -> null
        }
    }

    fun deleteHabit(id: Int) {
        _habits.update { list -> list.filter { it.id != id } }
    }

    fun addHabit(name: String) {
        if (validate(name) != null) return
        val id = nextId++
        _habits.update { list -> list + Habit(id, name.trim()) }
    }
}