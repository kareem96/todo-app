package com.example.todoapp.utils

open class Event<out T>(private val content: T) {
    var hashBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hashBeenHandled) {
            null
        } else {
            hashBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}