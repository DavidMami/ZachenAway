package com.example.zachenaway.data.repository

import com.example.zachenaway.data.Listener
import com.example.zachenaway.data.model.schema.User

class UserFirebaseRepository : FirebaseRepository() {

    fun getAllUsersSince(callback: Listener<List<User>>) {
        db.collection(User.COLLECTION).get()
            .addOnCompleteListener { task ->
                val list = mutableListOf<User>()
                if (task.isSuccessful) {
                    val jsonList = task.result
                    jsonList?.forEach { json ->
                        json.data?.let { data ->
                            list.add(User.fromJson(data))
                        }
                    }
                }
                callback.onComplete(list)
            }
    }

    fun addUser(user: User, listener: Listener<Void>) {
        db.collection(User.COLLECTION)
            .document(user.id)
            .set(user.toJson())
            .addOnCompleteListener {
                listener.onComplete(null)
            }
    }

    fun getUserById(id: String, callback: Listener<User?>) {
        db.collection(User.COLLECTION)
            .whereEqualTo("id", id)
            .get()
            .addOnCompleteListener { task ->
                var user: User? = null
                if (task.isSuccessful) {
                    val jsonList = task.result
                    jsonList?.forEach { json ->
                        json.data?.let { data ->
                            user = User.fromJson(data)
                        }
                    }
                }
                callback.onComplete(user)
            }
    }

    fun updateUser(user: User, callback: Listener<Void>) {
        val jsonUser = user.toJson()
        db.collection("Users")
            .document(user.id)
            .update(jsonUser)
            .addOnSuccessListener {
                callback.onComplete(null)
            }
    }
}
