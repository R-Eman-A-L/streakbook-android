package com.example.streakbook

import com.example.streakbook.data.FakeClock
import com.example.streakbook.data.FakeHabitRepository
import com.example.streakbook.model.Habit
import com.example.streakbook.ui.habitdetail.HabitDetailViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class HabitDetailViewModelTest {

    private lateinit var habitRepository: FakeHabitRepository
    private lateinit var clock: FakeClock

    @Before
    fun setUp() {
        val habits = listOf(
            Habit(1, "Read"),
            Habit(2, "Gym", setOf(LocalDate.of(2026, 10, 30))),
            Habit(3, "Water"),
            Habit(4, "Walk")
        )

        habitRepository = FakeHabitRepository(habits = habits)
        clock = FakeClock(LocalDate.of(2026, 11, 16))
    }

    @Test
    fun `An id that doesn't exist shows "not found"`() {
        val viewModel =
            HabitDetailViewModel(habitId = 99, repository = habitRepository, clock = clock)

        assertTrue(viewModel.state.value.isNotFound)
    }

    @Test
    fun `A habit done yesterday and the day before, but not today, has 2-day streak`() {
        val habits = listOf(
            Habit(
                id = 2,
                name = "Gym",
                completedDates = setOf(
                    LocalDate.of(
                        2026, 11, 14
                    ),
                    LocalDate.of(
                        2026, 11, 15
                    )
                )
            )
        )

        habitRepository = FakeHabitRepository(habits)
        val viewModel = HabitDetailViewModel(2, repository = habitRepository, clock = clock)

        assertEquals(2, viewModel.state.value.currentStreak)
        assertFalse(viewModel.state.value.doneToday)
    }

}