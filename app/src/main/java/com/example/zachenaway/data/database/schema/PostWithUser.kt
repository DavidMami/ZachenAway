package com.example.zachenaway.data.database.schema

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class PostWithUser(
    @Embedded
    val post: Post,
    @Relation(parentColumn = "userId", entityColumn = "id")
    val user: User
) : Serializable
