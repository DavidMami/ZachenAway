package com.example.zachenaway.data.model

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zachenaway.data.Listener
import com.example.zachenaway.data.LoadingState

import com.example.zachenaway.data.repository.PostFirebaseModel
import com.example.zachenaway.data.database.DBImplementation
import com.example.zachenaway.data.database.schema.PostWithUser
import com.example.zachenaway.data.database.schema.Post

class PostModel private constructor() {

    private val db: DBImplementation = DBImplementation.getInstance()
    private val postFirebaseModel = PostFirebaseModel()

    private val posts = MutableLiveData<List<Post>>()
    private val postsWithUser = MutableLiveData<List<PostWithUser>>()
    private val userPosts = MutableLiveData<List<Post>?>()

    private val postsListLoadingState = MutableLiveData(LoadingState.NOT_LOADING)
    private val postsWithUserListLoadingState = MutableLiveData(LoadingState.NOT_LOADING)
    private val userPostsListLoadingState = MutableLiveData(LoadingState.NOT_LOADING)

    companion object {
        val instance = PostModel()
    }

    fun getAllPostsWithUser(): LiveData<List<PostWithUser>> {
        if (postsWithUser.value == null) refreshAllPostsWithUser()
        return postsWithUser
    }

    fun getAllUserPosts(): MutableLiveData<List<Post>?> {
        if (userPosts.value == null) refreshUserPosts {}
        return userPosts
    }

    fun refreshAllPosts(listener: Listener<Unit>) {
        postsListLoadingState.postValue(LoadingState.LOADING)
        postFirebaseModel.getAllPosts { list ->
            db.executor.execute {
                if (list != null) {
                    updatePostsInRoom(list)
                }
                posts.postValue(db.getPostDao().getAll())
                postsListLoadingState.postValue(LoadingState.NOT_LOADING)
                listener.onComplete(Unit)
            }
        }
    }

    fun refreshAllPostsWithUser() {
        postsWithUserListLoadingState.postValue(LoadingState.LOADING)
        refreshUserPosts {
            db.executor.execute {
                val postsList = db.getPostDao().getPostsWithUser()
                postsWithUser.postValue(postsList)
                postsWithUserListLoadingState.postValue(LoadingState.NOT_LOADING)
            }
        }
    }

    fun refreshUserPosts(listener: Listener<Unit>) {
        userPostsListLoadingState.postValue(LoadingState.LOADING)
        UserModel.instance().refreshAllUsers {
            refreshAllPosts {
                db.executor.execute {
                    val userPostsList =
                        UserModel.instance().getCurrentUserId()
                            ?.let { it1 -> db.getPostDao().getPostsByUserId(it1) }
                    userPosts.postValue(userPostsList)
                    userPostsListLoadingState.postValue(LoadingState.NOT_LOADING)
                    listener.onComplete(Unit)
                }
            }
        }
    }

    private fun updatePostsInRoom(posts: List<Post>) {
        posts.forEach { post ->
            db.executor.execute {
                db.getPostDao().insertAll(post)
            }
        }
    }

    fun addPost(post: Post) {
        postFirebaseModel.addPost(post) {
            db.executor.execute {
                db.getPostDao().insert(post)
                refreshAllPostsWithUser()
            }
        }
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: Listener<String>) {
        postFirebaseModel.uploadImage(name, bitmap, listener)
    }

    fun updatePost(post: Post) {
        postFirebaseModel.updatePost(post) {
            db.executor.execute {
                db.getPostDao().update(post)
                refreshAllPostsWithUser()
            }
        }
    }

    fun deletePost(postId: String, imageName: String, listener: Listener<Unit>) {
        postFirebaseModel.deletePost(postId, imageName) { success ->
            if (success == true) {
                db.executor.execute {
                    db.getPostDao().deleteById(postId)
                    refreshAllPostsWithUser()
                    listener.onComplete(Unit)
                }
            } else {
                listener.onComplete(Unit)
            }
        }
    }

    fun startListeningForPosts() {
        postFirebaseModel.listenForPostsUpdates(object : Listener<List<Post>> {
            override fun onComplete(value: List<Post>?) {
                db.executor.execute {
                    if (value != null) {
                        updatePostsInRoom(value)
                    }

                    postsWithUser.postValue(db.getPostDao().getPostsWithUser())
                }
            }

            override fun onError(error: String?) {
                // Handle the error if needed
            }
        })
    }
}
