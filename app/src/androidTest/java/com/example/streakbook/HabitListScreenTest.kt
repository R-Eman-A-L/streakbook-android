package com.example.streakbook


import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.example.streakbook.data.FakeClock
import com.example.streakbook.data.FakeHabitRepository
import com.example.streakbook.data.FakeLogger
import com.example.streakbook.data.FakeQuoteRepository
import com.example.streakbook.ui.habitlist.HabitListScreen
import com.example.streakbook.ui.habitlist.HabitListViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HabitListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun showScreen(onOpenHabit: (Int) -> Unit = {}){
        val viewModel = HabitListViewModel(
            repository = FakeHabitRepository(),
            clock = FakeClock(LocalDate.of(2026, 11, 16)),
            quoteRepository = FakeQuoteRepository(),
            logger = FakeLogger()
        )
        composeTestRule.setContent { HabitListScreen(
            viewModel = viewModel,
            onOpenHabit = onOpenHabit
        ) }
    }

    @Test
    fun tapping_a_habit_asks_to_open_it() {
        var openedId: Int? = null                              // Arrange: the "fake navigation"
        showScreen(onOpenHabit = { id -> openedId = id })

        composeTestRule.onNodeWithText("Gym").performClick()  // Act: tap the row

        assertEquals(2, openedId)                              // Assert: it asked for Gym's id
    }

    @Test
    fun checking_a_habit_updates_the_done_count(){
        showScreen()
        composeTestRule.onNodeWithText("0 of 4 done today").assertIsDisplayed()


        composeTestRule.onNodeWithTag("check-1").performClick()

        composeTestRule.onNodeWithTag("check-1").assertIsOn()
        composeTestRule.onNodeWithText("1 of 4 done today").assertIsDisplayed()
        composeTestRule.onNodeWithText("0 of 4 done today").assertDoesNotExist()
    }
    @Test
    fun long_press_habit_remove_it_from_screen(){
        showScreen()

        composeTestRule.onNodeWithText("Gym").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Gym").assertDoesNotExist()

    }
}