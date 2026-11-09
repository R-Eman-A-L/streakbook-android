package com.example.streakbook.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class HabitForm(val id: Int, val name: String, val completedDates: List<String>) {
    fun toHabit(): Habit = Habit(
        id = id,
        name = name,
        completedDates = completedDates.map {
            LocalDate.parse(it)
        }.toSet()
    )
}
