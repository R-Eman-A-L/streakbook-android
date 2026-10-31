package com.example.streakbook.data

import com.example.streakbook.model.Habit
import java.time.LocalDate

interface HabitRepository {
    fun getHabits() : List<Habit>
    fun add(name: String)
    fun toggle(id: Int, date: LocalDate)
    fun delete(id: Int)
    fun getHabit(id: Int) : Habit?
}