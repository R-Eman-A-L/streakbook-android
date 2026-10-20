package com.example.streakbook

interface HabitRepository {
    fun getHabits() : List<Habit>
    fun add(name: String)
    fun toggle(id: Int)
    fun delete(id: Int)
}