package com.example.zachenaway.data.database

import com.example.zachenaway.data.database.dao.UserDao
import com.example.zachenaway.data.database.schema.User

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zachenaway.ui.main.ZachenAwayApplication

@Database(entities = [User::class], version = 103, exportSchema = true)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun userDao(): UserDao
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
