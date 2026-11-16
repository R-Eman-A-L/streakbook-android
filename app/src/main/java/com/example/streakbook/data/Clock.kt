package com.example.streakbook.data

import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface Clock {
    fun today(): LocalDate
}
@Singleton
class SystemClock @Inject constructor(): Clock {
    override fun today(): LocalDate = LocalDate.now()
}

class FakeClock(private  val date: LocalDate) : Clock {
    override fun today(): LocalDate = date
}