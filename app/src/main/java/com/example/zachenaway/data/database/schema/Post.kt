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
    var city: String = "",
    var street: String = "",
    var description: String = "",
    var category: String = "",
    var userId: String = "",
    var image: String = "",
) : Serializable {

    constructor(obj: Map<String, Any>) : this(
        id = obj["id"] as? String ?: "",
        city = obj["city"] as? String ?: "",
        street = obj["street"] as? String ?: "",
        description = obj["description"] as? String ?: "",
        category = obj["category"] as? String ?: "",
        userId = obj["userId"] as? String ?: "",
        image = obj["image"] as? String ?: "",
    )

    constructor(
        id: String,
        city: String,
        street: EditText,
        description: EditText,
        category: String,
        userId: String,
        image: String,
    ) : this(
        id = id,
        city = city,
        street = street.text.toString(),
        description = description.text.toString(),
        category = category,
        userId = userId,
        image = image,
    )

    fun toJson(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "city" to city,
            "street" to street,
            "description" to description,
            "category" to category,
            "userId" to userId,
            "image" to image,
        )
    }
}
