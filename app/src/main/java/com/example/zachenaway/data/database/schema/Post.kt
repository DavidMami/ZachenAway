package com.example.zachenaway.data.database.schema

import android.widget.EditText
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// TODO: Work with constants for the keys
@Entity
data class Post(
    @PrimaryKey
    var id: String = "",
    var location: String = "",
    var description: String = "",
    var category: String = "",
    var userId: String = "",
    var image: String = "",
) : Serializable {

    constructor(obj: Map<String, Any>) : this(
        id = obj["id"] as? String ?: "",
        location = obj["location"] as? String ?: "",
        description = obj["description"] as? String ?: "",
        category = obj["category"] as? String ?: "",
        userId = obj["userId"] as? String ?: "",
        image = obj["image"] as? String ?: "",
    )

    constructor(
        id: String,
        location: EditText,
        description: EditText,
        category: String,
        userId: String,
        image: String,
    ) : this(
        id = id,
        location = location.text.toString(),
        description = description.text.toString(),
        category = category,
        userId = userId,
        image = image,
    )

    fun toJson(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "location" to location,
            "description" to description,
            "category" to category,
            "userId" to userId,
            "image" to image,
        )
    }
}
