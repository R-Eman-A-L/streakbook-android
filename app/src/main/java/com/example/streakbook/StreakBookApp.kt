package com.example.streakbook

import android.app.Application
import com.example.streakbook.data.Clock
import com.example.streakbook.data.FakeQuoteRepository
import com.example.streakbook.data.NetworkQuoteRepository
import com.example.streakbook.data.QuoteRepository
import com.example.streakbook.data.SavedHabitRepository
import com.example.streakbook.data.SystemClock
import com.example.streakbook.data.habitStore
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class StreakBookApp : Application() {
    val appScope = CoroutineScope(SupervisorJob())
    lateinit var habitRepository: SavedHabitRepository
    val clock: Clock = SystemClock()
    val httpClient = HttpClient(OkHttp){
        install(HttpTimeout) {requestTimeoutMillis = 10_000}
    }

    val quoteRepository: QuoteRepository = NetworkQuoteRepository(httpClient)

    override fun onCreate() {
        super.onCreate()
        habitRepository = SavedHabitRepository(habitStore, appScope)
    }
}