package com.example.zachenaway.data.repository

import com.example.zachenaway.data.Listener
import com.example.zachenaway.data.database.schema.Post

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class PostFirebaseModel : FirebaseRepository() {

    fun getAllPosts(callback: Listener<List<Post>>) {
        db.collection("Posts").get()
            .addOnCompleteListener { task ->
                val list = mutableListOf<Post>()
                if (task.isSuccessful) {
                    val jsonList: QuerySnapshot? = task.result
                    jsonList?.forEach { json: DocumentSnapshot ->
                        json.data?.let { data ->
                            list.add(Post(data))
                        }
                    }
                }
                callback.onComplete(list)
            }
    }

    fun addPost(post: Post, listener: Listener<Unit>) {
        db.collection("Posts").document(post.id).set(post.toJson())
            .addOnCompleteListener { listener.onComplete(Unit) }
    }

    fun updatePost(post: Post, callback: Listener<Unit>) {
        db.collection("Posts").document(post.id)
            .update(post.toJson())
            .addOnSuccessListener { callback.onComplete(Unit) }
    }
}
