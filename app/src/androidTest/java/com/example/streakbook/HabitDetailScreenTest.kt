package com.example.streakbook

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.streakbook.data.FakeClock
import com.example.streakbook.data.FakeHabitRepository
import com.example.streakbook.ui.habitdetail.HabitDetailScreen
import com.example.streakbook.ui.habitdetail.HabitDetailViewModel
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class HabitDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun the_detail_screen_shows_the_habit() {
        val viewModel = HabitDetailViewModel(
            habitId = 2,
            repository = FakeHabitRepository(),     // default habits: id 2 = Gym
            clock = FakeClock(LocalDate.of(2026, 11, 16))
        )
        composeTestRule.setContent {
            HabitDetailScreen(
                viewModel = viewModel,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Gym").assertIsDisplayed()
        composeTestRule.onNodeWithText("The status of the habit is: Not done yet").assertIsDisplayed()
        // + one text that ONLY the detail screen shows (for example its streak label)
    }
}