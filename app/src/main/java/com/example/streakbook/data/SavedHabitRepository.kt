package com.example.streakbook.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.streakbook.model.Habit
import com.example.streakbook.model.HabitForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

// ===== 3. PLAN: SavedHabitRepository =====
// What InMemoryHabitRepository keeps (its fields): the habit list, nextId
// So the notebook must hold: the habit list AND nextId
//
// When the app starts (the repository is created)
// Goal: its fields hold the saved habits and nextId if the notebook had data,
//       or the 4 starting habits and their nextId if it was empty.
//       That way getHabits() can answer.
// Have: the notebook, the fields (the list, nextId), the starting habits
//   1. open the notebook and check whether it has saved data
//   2. if it has data: put the saved habits and nextId into the fields
//   3. if it's empty (first time ever): put the starting habits and their nextId into the fields
//
// After add, toggle or delete
// Goal: the notebook holds the same habits and nextId as the fields,
//       so after closing and reopening, the user sees exactly what they left.
// Have: the fields, the notebook, and the parameters:
//       name (add), id + date (toggle), id (delete)
//   1. change the fields the same way InMemoryHabitRepository does (add / toggle / delete)
//   2. save(): write the habits AND nextId to the notebook
//
// save()   (helper, called at the end of add, toggle and delete — written once, DRY)
// Goal: the notebook holds exactly what the fields hold.
// Have: the fields, the notebook
//   1. write the habit list and nextId to the notebook

// ===== 4. TEST PREDICTIONS (do this → close and reopen → expect) =====
//   - add "Read" → close and reopen → "Read" is still in the list
//   - check Gym and Read → close and reopen → both still checked, and "2 of 5 done today" is still right
//   - add "Read" → close and reopen → add "Walk" → delete "Walk"
//       → expect only Walk gone, Read still there
//       (if nextId wasn't saved, Walk would get Read's id, and deleting Walk would delete Read too)
//   - first time ever (uninstall, then run) → expect the 4 starting habits, none checked

// Android: load() (suspend) does the "when the app starts" part, called from MainActivity
//   in lifecycleScope before setContent; guard: already loaded → stop (rotation).
// save() writes both pages in ONE store.edit, on a ticket in appScope (from StreakBookApp).
@Singleton
class SavedHabitRepository @Inject constructor(
    private val store: DataStore<Preferences>,
    private val appScope: CoroutineScope,
) : HabitRepository {
    private lateinit var habits: List<Habit>
    private var nextId = 0
    private var loaded = false
    override fun getHabits(): List<Habit> = habits

    override fun add(name: String) {
        val id = nextId++
        habits = habits + Habit(id, name.trim())
        save()
    }

    override fun toggle(id: Int, date: LocalDate) {
        habits = habits.map {
            if (it.id == id) {
                if (date in it.completedDates) {
                    it.copy(completedDates = it.completedDates - date)
                } else {
                    it.copy(completedDates = it.completedDates + date)
                }
            } else it
        }
        save()
    }

    override fun delete(id: Int) {
        habits = habits.filter { it.id != id }
        save()
    }

    override fun getHabit(id: Int): Habit? {
        return habits.find { it.id == id }
    }

    suspend fun load() {
        if (loaded) return

        val pages = store.data.first()
        val habitsText = pages[HABITS]
        if (habitsText == null) {
            habits = listOf(
                Habit(1, "Read"),
                Habit(2, "Gym", setOf(LocalDate.of(2026, 10, 30))),
                Habit(3, "Water"),
                Habit(4, "Walk")
            )
            nextId = 5
        } else {

            habits = Json.decodeFromString<List<HabitForm>>(habitsText).map { it.toHabit() }
            nextId = pages[NEXT_ID] ?: ((habits.maxOfOrNull { it.id } ?: 0) + 1)

        }
        loaded = true


    }

    private fun save() {
        val habitsText = Json.encodeToString(habits.map { it.toForm() })
        val idToSave = nextId

        appScope.launch {
            store.edit { pages ->
                pages[HABITS] = habitsText
                pages[NEXT_ID] = idToSave
            }
        }
    }
}