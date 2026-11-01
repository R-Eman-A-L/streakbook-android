package com.example.streakbook

import android.app.Application
import com.example.streakbook.data.Clock
import com.example.streakbook.data.FakeClock
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.InMemoryHabitRepository
import com.example.streakbook.data.SystemClock
import java.time.LocalDate

class StreakBookApp : Application() {
    val habitRepository: HabitRepository = InMemoryHabitRepository()
    val clock: Clock = FakeClock(LocalDate.of(2026,10,31))
}