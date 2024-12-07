package com.example.zachenaway.data.repository

import com.example.zachenaway.data.Listener

import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository private constructor() {

    private val authProvider: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private var instance: AuthRepository? = null

        fun getInstance(): AuthRepository {
            if (instance == null) {
                instance = AuthRepository()
            }
            return instance!!
        }
    }

    fun getUser(): FirebaseUser? {
        return authProvider.currentUser
    }

    fun getUserUid(): String? {
        val user = getUser()

        return user?.uid
    }

    fun isEmailAndPasswordValid(email: EditText, password: EditText): Boolean {
        return isEmailAndPasswordValid(email.text.toString(), password.text.toString())
    }

    fun isEmailAndPasswordValid(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun register(email: EditText, password: EditText, onCreateUser: Listener<FirebaseUser>) {
        register(email.text.toString(), password.text.toString(), onCreateUser)
    }

    fun register(email: String, password: String, onCreateUser: Listener<FirebaseUser>) {
        authProvider.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onCompleteListener(task, onCreateUser)
            }
    }

    fun login(email: EditText, password: EditText, onCreateUser: Listener<FirebaseUser>) {
        login(email.text.toString(), password.text.toString(), onCreateUser)
    }

    fun login(email: String, password: String, onCreateUser: Listener<FirebaseUser>) {
        authProvider.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onCompleteListener(task, onCreateUser)
            }
    }

    fun signOut() {
        authProvider.signOut()
    }

    private fun onCompleteListener(task: Task<*>, onCreateUser: Listener<FirebaseUser>) {
        if (task.isSuccessful) {
            val user = authProvider.currentUser
            onCreateUser.onComplete(user)
        } else {
            onCreateUser.onComplete(null)
        }
    }
}
