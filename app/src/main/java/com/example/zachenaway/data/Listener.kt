package com.example.zachenaway.data;

fun interface Listener<T> {
    fun onComplete(value: T?)
}
