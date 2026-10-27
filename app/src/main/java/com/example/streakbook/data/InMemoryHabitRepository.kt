package com.example.streakbook.data

import com.example.streakbook.model.Habit

class InMemoryHabitRepository : HabitRepository {
    private var habits =
        listOf(
            Habit(1, "Read"),
            Habit(2, "Gym"),
            Habit(3, "Water"),
            Habit(4, "Walk")
        )


    private var nextId = 5

    override fun getHabits(): List<Habit> = habits

    override fun add(name: String) {
        val id = nextId++
        habits =  habits + Habit(id, name.trim())
    }

    override fun toggle(id: Int) {
        habits = habits.map {
            if (it.id == id)
                it.copy(doneToday = !it.doneToday) else it
        }

    }

    override fun delete(id: Int) {
        habits = habits.filter { it.id != id }
    }

}