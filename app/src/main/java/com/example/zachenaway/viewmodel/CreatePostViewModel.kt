package com.example.zachenaway.viewmodel

import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.database.schema.User
import com.example.zachenaway.data.model.PostModel
import com.example.zachenaway.data.model.UserModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {

    var user: LiveData<User?>? = null

    init {
        UserModel.instance().getSignedUser { fetchedUser ->
            user = fetchedUser
        }
    }

    fun getUser(): LiveData<User?>? {
        return user;
    }

    fun addPost(post: Post, postImageBitmap: Bitmap) {
        PostModel.instance.uploadImage(post.id, postImageBitmap) { url ->
            setPostImage(post, url)
            PostModel.instance.addPost(post)
        }
    }

    private fun setPostImage(post: Post, url: String?) {
        if (url != null) {
            post.image = url
        }
    }
}
