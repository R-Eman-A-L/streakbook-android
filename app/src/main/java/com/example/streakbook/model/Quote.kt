package com.example.streakbook.model

import kotlinx.serialization.Serializable

data class Quote(val text: String, val author: String)

@Serializable
data class QuoteForm(val id: Int, val quote: String, val author: String){
    fun toQuote(): Quote = Quote(text = quote, author = author)
}
