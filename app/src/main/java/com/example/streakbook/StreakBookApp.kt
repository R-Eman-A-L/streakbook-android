package com.example.streakbook

import android.app.Application
import com.example.streakbook.data.Clock
import com.example.streakbook.data.SavedHabitRepository
import com.example.streakbook.data.SystemClock
import com.example.streakbook.data.habitStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class StreakBookApp : Application() {
    val appScope = CoroutineScope(SupervisorJob())
    lateinit var habitRepository: SavedHabitRepository
    val clock: Clock = SystemClock()

    override fun onCreate() {
        super.onCreate()
        habitRepository = SavedHabitRepository(habitStore, appScope)
    }
}