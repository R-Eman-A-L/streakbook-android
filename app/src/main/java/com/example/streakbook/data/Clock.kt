package com.example.streakbook.data

import java.time.LocalDate

interface Clock {
    fun today(): LocalDate
}

class SystemClock: Clock {
    override fun today(): LocalDate = LocalDate.now()
}

class FakeClock(private  val date: LocalDate) : Clock {
    override fun today(): LocalDate = date
}