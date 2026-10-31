package com.example.streakbook.data

import com.example.streakbook.model.Habit
import java.time.LocalDate

class InMemoryHabitRepository : HabitRepository {
    private var habits =
        listOf(
            Habit(1, "Read"),
            Habit(2, "Gym", setOf(LocalDate.of(2026, 10, 30))),
            Habit(3, "Water"),
            Habit(4, "Walk")
        )


    private var nextId = 5

    override fun getHabits(): List<Habit> = habits

    override fun add(name: String) {
        val id = nextId++
        habits =  habits + Habit(id, name.trim())
    }

    override fun toggle(id: Int, date: LocalDate) {
        habits = habits.map {
            if (it.id == id){
                if (date in it.completedDates){
                    it.copy(completedDates = it.completedDates - date)
                } else{
                    it.copy(completedDates = it.completedDates + date)
                }
            } else it
        }

    }

    override fun delete(id: Int) {
        habits = habits.filter { it.id != id }
    }

    override fun getHabit(id: Int): Habit? {
        return habits.find { it.id == id }
    }

}