package com.example.zachenaway.viewmodel

import com.example.zachenaway.data.database.schema.PostWithUser
import com.example.zachenaway.data.model.PostModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class HomePageViewModel : ViewModel() {

//    init {
//        PostModel.instance.startListeningForPosts()
//    }

    private val postsWithUser: LiveData<List<PostWithUser>> =
        PostModel.instance.getAllPostsWithUser()

    fun getAllPostsWithUser(): LiveData<List<PostWithUser>> {
        return postsWithUser
    }
}
