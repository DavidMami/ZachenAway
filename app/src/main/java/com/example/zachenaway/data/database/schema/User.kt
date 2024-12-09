package com.example.zachenaway.data.database.schema

import android.widget.EditText
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey
    var id: String,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var imageUrl: String? = null
) : Serializable {

    companion object {
        const val COLLECTION = "Users"
        const val ID = "id"
        const val EMAIL = "email"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val IMAGE_URL = "imageUrl"

        fun fromJson(json: Map<String, Any?>): User {
            return User(
                id = json[ID] as String,
                email = json[EMAIL] as? String,
                firstName = json[FIRST_NAME] as? String,
                lastName = json[LAST_NAME] as? String,
                imageUrl = json[IMAGE_URL] as? String
            )
        }
    }

    constructor(
        id: String,
        email: EditText,
        firstName: EditText,
        lastName: EditText,
        imageUrl: String?
    ) : this(
        id,
        email.text.toString(),
        firstName.text.toString(),
        lastName.text.toString(),
        imageUrl
    )

    constructor(user: User) : this(
        id = user.id,
        email = user.email,
        firstName = user.firstName,
        lastName = user.lastName,
        imageUrl = user.imageUrl
    )

    constructor(user: FirebaseUser) : this(
        id = user.uid,
        email = user.email,
        firstName = user.displayName,
        lastName = user.displayName,
        imageUrl = ""
    )

    fun getName(): String = "${firstName ?: ""} ${lastName ?: ""}".trim()

    fun toJson(): Map<String, Any?> {
        return mapOf(
            ID to id,
            FIRST_NAME to firstName,
            LAST_NAME to lastName,
            EMAIL to email,
            IMAGE_URL to imageUrl
        )
    }
}
