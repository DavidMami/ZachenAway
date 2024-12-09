package com.example.zachenaway.data.database.dao

import com.example.zachenaway.data.database.schema.User

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): User?

    @Insert
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: User)

    @Update
    fun update(user: User)
}
