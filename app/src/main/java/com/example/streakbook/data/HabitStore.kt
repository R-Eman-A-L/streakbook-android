package com.example.streakbook.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.habitStore: DataStore<Preferences> by preferencesDataStore(name = "habits_store")

val HABITS = stringPreferencesKey("habits")
val NEXT_ID = intPreferencesKey("nextId")