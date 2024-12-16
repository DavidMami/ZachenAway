package com.example.zachenaway.data.model

import com.example.zachenaway.data.database.DBImplementation
import com.example.zachenaway.data.Listener
import com.example.zachenaway.data.LoadingState
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.data.repository.UserFirebaseRepository
import com.example.zachenaway.data.database.schema.User

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ExecutorService

class UserModel private constructor() {

    private val db: DBImplementation = DBImplementation.getInstance()
    private val firebaseUser = UserFirebaseRepository()
    private val currentUser = MutableLiveData<User?>()
    private val users = MutableLiveData<List<User>>()
    val usersListLoadingState = MutableLiveData(LoadingState.NOT_LOADING)

    companion object {
        @Volatile
        private var instance: UserModel? = null

        fun instance(): UserModel {
            return instance ?: synchronized(this) {
                instance ?: UserModel().also { instance = it }
            }
        }
    }

    fun getCurrentUserId(): String? {
        return AuthRepository.getInstance().getUserUid()
    }

    fun isSignedIn(): Boolean {
        return AuthRepository.getInstance().getUser() != null
    }

    fun getSignedUser(listener: Listener<LiveData<User?>>) {
        val currentUserId = getCurrentUserId()
        if (currentUser.value == null || currentUser.value?.id != currentUserId) {
            refreshAllUsers { listener.onComplete(currentUser) }
        } else {
            listener.onComplete(currentUser)
        }
    }

    fun setSignedUser() {
        db.executor.execute {
            val user = getCurrentUserId()?.let { db.getUserDao().findById(it) }
            if (user == null) {
                refreshAllUsers {
                    postProfileUser(getCurrentUserId()?.let { it1 -> db.getUserDao().findById(it1) })
                }
            } else {
                PostModel.instance.refreshUserPosts {
                    postProfileUser(user)
                }
            }
        }
    }

    private fun postProfileUser(user: User?) {
        currentUser.postValue(user)
    }

    fun signUserOut() {
        AuthRepository.getInstance().signOut()
        db.executor.execute {
            postProfileUser(null)
        }
    }

    fun refreshAllUsers(listener: Listener<Unit>) {
        usersListLoadingState.postValue(LoadingState.LOADING)

        firebaseUser.getAllUsersSince { list ->
            db.executor.execute {
                if (list != null) {
                    updateUsersInRoom(list)
                }
                users.postValue(db.getUserDao().getAll())
                postProfileUser(getCurrentUserId()?.let { db.getUserDao().findById(it) })
                usersListLoadingState.postValue(LoadingState.NOT_LOADING)
                listener.onComplete(Unit)
            }
        }
    }

    private fun updateUsersInRoom(users: List<User>) {
        for (user in users) {
            db.executor.execute {
                db.getUserDao().insertAll(user)
            }
        }
    }

    fun addUser(user: User) {
        firebaseUser.addUser(user) {
            db.executor.execute {
                db.getUserDao().insert(user)
                refreshAllUsers {}
            }
        }
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: Listener<String>) {
        firebaseUser.uploadImage(name, bitmap, listener)
    }

    fun updateUser(user: User, listener: Listener<Unit>) {
        firebaseUser.updateUser(user) {
            db.executor.execute {
                db.getUserDao().update(user)
                PostModel.instance.refreshAllPostsWithUser()
            }
            listener.onComplete(Unit)
        }
    }
}
