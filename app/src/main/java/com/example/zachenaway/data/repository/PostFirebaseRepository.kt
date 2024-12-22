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

    fun deletePost(postId: String, imageName: String, callback: Listener<Boolean>) {
        deleteImage(imageName) { imageDeleted ->
            if (imageDeleted == true) {
                db.collection("Posts").document(postId).delete()
                    .addOnSuccessListener {
                        callback.onComplete(true)
                    }
                    .addOnFailureListener {
                        callback.onComplete(false)
                    }
            } else {
                callback.onComplete(false)
            }
        }
    }

        fun listenForPostsUpdates(callback: Listener<List<Post>>) {
        db.collection("Posts").addSnapshotListener { snapshot, error ->
            if (error != null) {
                callback.onError("Error listening for updates: ${error.message}")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val posts = mutableListOf<Post>()

                snapshot.documents.forEach { document ->
                    document.data?.let { data ->
                        posts.add(Post(data))
                    }
                }

                callback.onComplete(posts)
            }
        }
    }

}
