package com.example.streakbook.data

import com.example.streakbook.model.Habit

interface HabitRepository {
    fun getHabits() : List<Habit>
    fun add(name: String)
    fun toggle(id: Int)
    fun delete(id: Int)
    fun getHabit(id: Int) : Habit?
}