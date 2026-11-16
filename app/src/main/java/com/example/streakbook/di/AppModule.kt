package com.example.streakbook.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.streakbook.data.Clock
import com.example.streakbook.data.HabitRepository
import com.example.streakbook.data.NetworkQuoteRepository
import com.example.streakbook.data.QuoteRepository
import com.example.streakbook.data.SavedHabitRepository
import com.example.streakbook.data.SystemClock
import com.example.streakbook.data.habitStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(HttpTimeout) { requestTimeoutMillis = 10_000 }
    }

    @Provides
    @Singleton
    fun provideHabitStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.habitStore

    @Provides @Singleton
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingsModule {
    @Binds abstract fun bindClock(impl: SystemClock): Clock
    @Binds abstract fun bindQuoteRepository(impl: NetworkQuoteRepository): QuoteRepository
    @Binds abstract fun bindHabitRepository(impl: SavedHabitRepository): HabitRepository
}