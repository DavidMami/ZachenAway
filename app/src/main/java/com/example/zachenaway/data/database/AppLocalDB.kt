package com.example.zachenaway.data.database

import com.example.zachenaway.data.database.dao.UserDao
import com.example.zachenaway.data.database.dao.PostDao
import com.example.zachenaway.data.database.schema.User
import com.example.zachenaway.data.database.schema.Post

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Post::class], version = 105, exportSchema = true)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}

object AppLocalDB {
    fun getAppDB(): AppLocalDbRepository {
        return Room.databaseBuilder(
            ZachenAwayApplication.getMyContext(),
            AppLocalDbRepository::class.java,
            "ZachenAway.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
