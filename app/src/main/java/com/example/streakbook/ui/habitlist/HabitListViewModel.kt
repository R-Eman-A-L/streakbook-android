package com.example.streakbook.ui.habitlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streakbook.data.AppLogger
import com.example.streakbook.data.Clock
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel @Inject constructor(
    private val repository: HabitRepository,
    private val clock: Clock,
    private val quoteRepository: QuoteRepository,
    private val logger: AppLogger
) : ViewModel() {

    private val _state = MutableStateFlow(HabitListUiState())
    val state: StateFlow<HabitListUiState> = _state.asStateFlow()

    init {
        loadQuote()
        refresh()
    }

    private fun refresh(error: String? = null) {
        val habits = repository.getHabits()
        val today = clock.today()

        val doneTodayIds = habits.filter { today in it.completedDates }.map { it.id }.toSet()

        _state.update {
            it.copy(
                habits = habits,
                doneTodayIds = doneTodayIds,
                doneCount = doneTodayIds.size,
                error = error,
            )
        }

    }

    private fun loadQuote() {
        _state.update { it.copy(quote = QuoteState.Loading) }

        viewModelScope.launch {
            try {
                val quote = quoteRepository.getQuote()
                _state.update { it.copy(quote = QuoteState.Success(quote)) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.error("Quote", "Quote failed", e)
                _state.update { it.copy(quote = QuoteState.Error("Couldn't load the quote")) }
            }
        }
    }

    fun onRetryQuote() {
        loadQuote()
    }

    fun onToggle(id: Int) {
        val today = clock.today()
        repository.toggle(id, today)
        refresh()
    }

    fun onDelete(id: Int) {
        repository.delete(id)
        refresh()
    }

    fun onAdd(name: String): Boolean {
        val trimmedName = name.trim()
        val problem = validate(trimmedName)
        if (problem != null) {
            refresh(problem)
            return false
        }
        repository.add(trimmedName)
        refresh()
        return true
    }

    fun onTextChanged() {
        _state.value = _state.value.copy(error = null)
    }

    private fun validate(name: String): String? = when {
        name.isEmpty() -> "Enter a habit name"
        repository.getHabits().any { it.name == name } -> "You already have that habit"
        else -> null
    }

}