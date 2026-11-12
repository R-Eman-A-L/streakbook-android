package com.example.streakbook.data

import com.example.streakbook.model.Quote
import com.example.streakbook.model.QuoteForm
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json


// ===== 4. STEPS: NetworkQuoteRepository.getQuote() =====
// Goal: gives back a Quote, or throws.
// Have: the HTTP client, the URL https://dummyjson.com/quotes/random
//   1. send a GET request to the URL                     ← throws by itself if no reply
//   2. if the status isn't 2xx → throw "Bad status: <the number>"   ← STOP
//   3. read the body → a QuoteForm (JSON tool)           ← throws by itself if unreadable
//   4. form.toQuote()
//   5. give back the Quote
// Throw by themselves, no step from me: no reply (no internet, timeout), unreadable body.


class NetworkQuoteRepository(private val client: HttpClient) : QuoteRepository {
    override suspend fun getQuote(): Quote {
        val response = client.get("https://dummyjson.com/quotes/random")

        if (response.status.value !in 200..299) throw IllegalStateException("Bad status: ${response.status.value}")

        return Json.decodeFromString<QuoteForm>(response.bodyAsText()).toQuote()

    }
}