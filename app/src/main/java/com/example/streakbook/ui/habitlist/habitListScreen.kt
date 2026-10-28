package com.example.streakbook.ui.habitlist

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.streakbook.ui.theme.StreakBookTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(viewModel: HabitListViewModel = viewModel(), onOpenHabit: (Int) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var inputText by rememberSaveable { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "StreakBook") }

            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Row {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        viewModel.onTextChanged()
                    },
                    label = { Text("Add Habit") },
                    isError = state.error != null,
                    supportingText = { state.error?.let { Text(it) } },
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = {
                    if (viewModel.onAdd(inputText)) inputText = ""

                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Habit")
                }
            }

            Text("${state.doneCount} of ${state.habits.size} done today")

            if (state.habits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No habits yet",
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(state.habits, key = { it.id }) { habit ->
                        ListItem(
                            headlineContent = { Text(text = habit.name) }, trailingContent = {

                                Checkbox(
                                    checked = habit.doneToday,
                                    onCheckedChange = {
                                        viewModel.onToggle(habit.id)
                                    }
                                )
                            }, modifier = Modifier.combinedClickable(
                                onClick = {
                                    onOpenHabit(habit.id)
                                },
                                onLongClick = {
                                    viewModel.onDelete(habit.id)
                                }
                            )
                        )

                    }
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitListPreview() {
    StreakBookTheme {

    }
}