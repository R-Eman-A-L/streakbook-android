package com.example.streakbook.model

import java.time.LocalDate

data class Habit(val id: Int, val name: String, val completedDates: Set<LocalDate> = emptySet())