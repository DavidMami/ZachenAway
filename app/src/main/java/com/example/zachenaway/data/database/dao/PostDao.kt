package com.example.zachenaway.data.database.dao

import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.database.schema.PostWithUser

import androidx.room.*

@Dao
interface PostDao {

    @Query("SELECT * FROM Post")
    fun getAll(): List<Post>

    @Query("SELECT * FROM Post WHERE id = :postId")
    fun getPostById(postId: String): Post?

    @Query("SELECT * FROM Post WHERE location = :location")
    fun getPostsByLocation(location: String): List<Post>

    @Transaction
    @Query("SELECT * FROM Post")
    fun getPostsWithUser(): List<PostWithUser>

    @Transaction
    @Query("SELECT * FROM Post WHERE userId = :userId")
    fun getPostsByUserId(userId: String): List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg posts: Post)

    @Insert
    fun insert(post: Post)

    @Delete
    fun delete(post: Post)

    @Update
    fun update(post: Post)
}
