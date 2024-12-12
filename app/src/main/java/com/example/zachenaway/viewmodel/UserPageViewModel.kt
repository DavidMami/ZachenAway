package com.example.zachenaway.viewmodel

import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.model.PostModel
import com.example.zachenaway.data.database.schema.User
import com.example.zachenaway.data.database.schema.Post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserPageViewModel : ViewModel() {
    private val userPosts: MutableLiveData<List<Post>?> = PostModel.instance.getAllUserPosts()
    private var user: LiveData<User?>? = null

    init {
        UserModel.instance().getSignedUser { u -> user = u }
    }

    fun getUserPosts(): MutableLiveData<List<Post>?> {
        return userPosts
    }

    fun getUser(): LiveData<User?>? {
        return user
    }
}
