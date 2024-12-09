package com.example.zachenaway.data.database

import com.example.zachenaway.data.database.dao.UserDao

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DBImplementation private constructor() {

    val executor: Executor = Executors.newSingleThreadExecutor()
    val localDB: AppLocalDbRepository = AppLocalDB.getAppDB()

    companion object {
        @Volatile
        private var instance: DBImplementation? = null

        fun getInstance(): DBImplementation {
            return instance ?: synchronized(this) {
                instance ?: DBImplementation().also { instance = it }
            }
        }
    }

    fun getUserDao(): UserDao {
        return localDB.userDao()
    }
}
