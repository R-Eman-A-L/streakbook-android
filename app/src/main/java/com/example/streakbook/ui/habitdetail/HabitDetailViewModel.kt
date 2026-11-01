package com.example.streakbook.ui.habitdetail

import androidx.lifecycle.ViewModel
import com.example.streakbook.data.Clock
import com.example.streakbook.data.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class HabitDetailViewModel(
    private val habitId: Int,
    private val repository: HabitRepository,
    private val clock: Clock
) : ViewModel() {
    private val _state = MutableStateFlow(HabitDetailUiState())
    val state: StateFlow<HabitDetailUiState> = _state.asStateFlow()


    init {
        load()
    }

    private fun load() {
        val habit = repository.getHabit(habitId)
        if (habit == null) {
            _state.value = HabitDetailUiState(isNotFound = true)
            return
        }

        val today = clock.today()
        val completedDates = habit.completedDates
        val doneToday = today in completedDates
        val last7Days = last7Days(completedDates, today)
        val currentStreak = currentStreak(completedDates, today)

        _state.value = HabitDetailUiState(
            habitName = habit.name,
            doneToday = doneToday,
            isNotFound = false,
            last7Days = last7Days,
            currentStreak = currentStreak,
        )
    }

    private fun last7Days(dates: Set<LocalDate>, today: LocalDate): List<Boolean>{
        val days = mutableListOf<Boolean>()
        var day = today.minusDays(6)

        repeat(7) {
            days += day in dates
            day = day.plusDays(1)
        }

        return days
    }

    private fun currentStreak(dates: Set<LocalDate>, today: LocalDate): Int{
        var streak = 0
        // variant var day = today.takeIf { date -> date in dates } ?: today.minusDays(1)
        var day = if (today in dates) today else today.minusDays(1)

        while (day in dates){
            streak++
            day = day.minusDays(1)
        }

        return streak
    }
}