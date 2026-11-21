package com.example.streakbook.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpTimeout
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class NetworkQuoteRepositoryTest {

    @Test
    fun `A 200 reply becomes a Quote with that text and author`() = runTest {
        val engine = MockEngine { request ->
            respond(
                content = """{"id": 1,"quote":"Stay hungry","author":"Steve Jobs"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val repository = NetworkQuoteRepository(HttpClient(engine) {
            install(HttpTimeout) { requestTimeoutMillis = 10_000 }
        })

        val quote = repository.getQuote()

        assertEquals("Stay hungry", quote.text)
        assertEquals("Steve Jobs", quote.author)
    }

    @Test
    fun `(R) A 500 reply throws instead of returning a quote`() = runTest {
        val engine = MockEngine { request ->
            respond(
                content = """{"id": 1,"quote":"Stay hungry","author":"Steve Jobs"}""",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val repository = NetworkQuoteRepository(HttpClient(engine){
            install(HttpTimeout) { requestTimeoutMillis = 10_000 }
        })

        try {
            repository.getQuote()
            fail("getQuote() should have thrown")
        } catch (e: Exception) {
            assertEquals("Bad status: 500", e.message)
        }
    }
}