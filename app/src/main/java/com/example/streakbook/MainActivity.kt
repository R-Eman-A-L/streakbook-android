package com.example.streakbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.streakbook.ui.theme.StreakBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreakBookTheme {
                HabitListScreen()

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen() {
    var habits by remember {
        mutableStateOf(
            listOf(
                Habit(1, "Read"),
                Habit(2, "Gym"),
                Habit(3, "Water"),
                Habit(4, "Walk")
            )
        )
    }
    var nextId by remember { mutableIntStateOf(5) }
    var inputText by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }



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
                        error = null
                    },
                    label = { Text("Add Habit") },
                    isError = error != null,
                    supportingText = { error?.let { Text(it) } },
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = {
                    val name = inputText.trim()
                    when {
                        name.isBlank() -> error = "Enter a habit name"
                        habits.any { it.name == name } -> error = "You already have that habit"
                        else -> {
                            habits += Habit(nextId++, name)
                            inputText = ""
                        }
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Habit")
                }
            }

            if (habits.isEmpty()) {
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
                    items(habits, key = { it.id }) { habit ->
                        ListItem(
                            headlineContent = { Text(text = habit.name) }, trailingContent = {
                                if (habit.doneToday) Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Done"
                                )
                            }, modifier = Modifier.combinedClickable(
                                onClick = {
                                    habits = habits.map {
                                        if (it.id == habit.id)
                                            it.copy(doneToday = !it.doneToday) else it
                                    }
                                },
                                onLongClick = {
                                    habits = habits.filter { it.id != habit.id }
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
        HabitListScreen()
    }
}