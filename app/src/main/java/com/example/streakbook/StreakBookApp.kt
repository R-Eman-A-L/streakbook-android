package com.example.streakbook

import android.app.Application
import com.example.streakbook.data.Clock
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.InMemoryHabitRepository
import com.example.streakbook.data.SystemClock

class StreakBookApp : Application() {
    val habitRepository: HabitRepository = InMemoryHabitRepository()
    val clock: Clock = SystemClock()
}