package com.example.streakbook

import android.app.Application
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.InMemoryHabitRepository

class StreakBookApp : Application() {
    val habitRepository: HabitRepository = InMemoryHabitRepository()
}