package com.example.streakbook

import com.example.streakbook.data.FakeClock
import com.example.streakbook.data.FakeHabitRepository
import com.example.streakbook.data.FakeLogger
import com.example.streakbook.data.FakeQuoteRepository
import com.example.streakbook.model.Habit
import com.example.streakbook.ui.habitlist.HabitListViewModel
import com.example.streakbook.ui.habitlist.QuoteState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class HabitListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var habitRepository: FakeHabitRepository
    private lateinit var clock: FakeClock
    private lateinit var quoteRepository: FakeQuoteRepository
    private lateinit var viewModel: HabitListViewModel
    private lateinit var logger: FakeLogger

    @Before
    fun setUp() {
        val habits = listOf(
            Habit(1, "Read"),
            Habit(2, "Gym", setOf(LocalDate.of(2026, 10, 30))),
            Habit(3, "Water"),
            Habit(4, "Walk")
        )
        habitRepository = FakeHabitRepository(habits)
        clock = FakeClock(LocalDate.of(2026, 11, 16))
        quoteRepository = FakeQuoteRepository()
        logger = FakeLogger()

        viewModel = HabitListViewModel(
            repository = habitRepository,
            clock = clock,
            quoteRepository = quoteRepository,
            logger = logger

        )
    }

    @Test
    fun `Adding a valid name adds a habit with that name and returns true`() {
        val result = viewModel.onAdd("Swim")
        val habits = viewModel.state.value.habits

        assertTrue(result)
        assertEquals(5, habits.size)
        assertTrue(habits.any { habit -> habit.name == "Swim" })

    }

    @Test
    fun `A name with only spaces is refused with an error`() {
        val result = viewModel.onAdd("   ")
        val habits = viewModel.state.value.habits

        assertFalse(result)
        assertEquals("Enter a habit name", viewModel.state.value.error)
        assertEquals(4, habits.size)
    }

    @Test
    fun `A duplicate name(even with a trailing space) is refused`() {
        val result = viewModel.onAdd("Water ")
        val habits = viewModel.state.value.habits

        assertFalse(result)
        assertEquals("You already have that habit", viewModel.state.value.error)
        assertEquals(4, habits.size)
    }

    @Test
    fun `(R) A name with spaces around it is saved without the spaces`() {
        val result = viewModel.onAdd(" Swim ")
        val habits = viewModel.state.value.habits

        assertTrue(result)
        assertEquals(5, habits.size)
        assertTrue(habits.any { it.name == "Swim" })
        assertEquals("Swim", habits.find { it.id == 5 }?.name)
    }

    @Test
    fun `Deleting a habit removes exactly that habit`() {
        viewModel.onDelete(1)
        val habits = viewModel.state.value.habits

        assertEquals(3, habits.size)
        assertFalse(habits.any { it.id == 1 })
        assertTrue(habits.any { it.name == "Walk" })
        assertTrue(habits.any { it.name == "Water" })
        assertTrue(habits.any { it.name == "Gym" })

    }

    @Test
    fun `(R) the add error clears when the user types again`() {
        viewModel.onAdd(" ")
        viewModel.onTextChanged()

        assertNull(viewModel.state.value.error)
    }


    @Test
    fun `Right after building, the quote is loading`() {
        val quote = viewModel.state.value.quote
        assertTrue(quote is QuoteState.Loading)
    }

    @Test
    fun `A successful quote load shows the quote`() = runTest {
        advanceUntilIdle()

        val quote = viewModel.state.value.quote

        assertTrue(quote is QuoteState.Success)

        val text = (quote as QuoteState.Success).quote.text

        assertTrue((text.startsWith("Follow the way of life")))
    }

    @Test
    fun `A failed quote load shows the error card, without crashing`() = runTest {
        val viewModel = HabitListViewModel(
            repository = habitRepository,
            clock = clock,
            quoteRepository = FakeQuoteRepository(shouldFail = true),
            logger = logger
        )

        advanceUntilIdle()

        val quote = viewModel.state.value.quote

        assertTrue(quote is QuoteState.Error)

        val errorMessage = (quote as QuoteState.Error).message

        assertEquals("Couldn't load the quote", errorMessage)

        assertEquals(1, logger.errors.size)

    }


    @Test
    fun `(R) Checking a habit keeps the quote`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.state.value.quote is QuoteState.Success)
        val before = (viewModel.state.value.quote as QuoteState.Success).quote.text

        viewModel.onToggle(1)

        assertTrue(viewModel.state.value.quote is QuoteState.Success)
        val after = (viewModel.state.value.quote as QuoteState.Success).quote.text

        assertEquals(before, after)
        assertEquals(1, viewModel.state.value.doneCount)
    }

}