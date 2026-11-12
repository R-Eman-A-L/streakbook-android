package com.example.streakbook.data

import com.example.streakbook.model.Quote

interface QuoteRepository {
    suspend fun getQuote(): Quote
}