package com.example.zachenaway.data;

fun interface Listener<T> {
    fun onComplete(value: T?)

    fun onError(error: String?) {
        // Default behavior: no-op
    }
}
