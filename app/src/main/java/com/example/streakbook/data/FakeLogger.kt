package com.example.streakbook.data

class FakeLogger : AppLogger{
    val errors = mutableListOf<String>()
    override fun error(tag: String, message: String, e: Throwable) {
        errors += message
    }
}