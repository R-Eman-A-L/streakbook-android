package com.example.streakbook.data

import com.example.streakbook.model.Quote
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class FakeQuoteRepository(private val shouldFail: Boolean = false) : QuoteRepository {
    override suspend fun getQuote(): Quote {
        delay(1000.milliseconds)
        if (shouldFail) throw IllegalStateException("Fake failure")

        return Quote(
            text = "Follow the way of life, which the Holy Prophet has shown you, for verily that is the right path.",
            author = "Abu Bakr (R.A)"
        )

    }
}