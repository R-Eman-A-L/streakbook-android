package com.example.streakbook.model

import java.time.LocalDate

data class Habit(val id: Int, val name: String, val completedDates: Set<LocalDate> = emptySet()) {
    fun toForm(): HabitForm = HabitForm(
        id = id,
        name = name,
        completedDates = completedDates.map {
            it.toString()
        }
    )
}
