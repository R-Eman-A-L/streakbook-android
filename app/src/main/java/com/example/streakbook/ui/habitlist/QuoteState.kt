package com.example.streakbook.ui.habitlist

import com.example.streakbook.model.Quote

sealed interface QuoteState {
    data object Loading : QuoteState
    data class Error(val message: String) : QuoteState
    data class Success(val quote: Quote) : QuoteState
}