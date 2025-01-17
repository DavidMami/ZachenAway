package com.example.zachenaway.data.repository

import com.example.zachenaway.data.Listener

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

abstract class FirebaseRepository {

    protected val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    protected val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {})
        }

        db.firestoreSettings = settings
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: Listener<String>) {
        val storageRef: StorageReference = storage.reference
//        val imagesRef = storageRef.child("images/$name.jpg")
        val imagesRef = storageRef.child("$name.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imagesRef.putBytes(data)

        uploadTask.addOnFailureListener {
            listener.onComplete(null)
        }.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                listener.onComplete(uri.toString())
            }
        }
    }

    fun deleteImage(name: String, listener: Listener<Boolean>) {
        val storageRef: StorageReference = storage.reference
        val imagesRef = storageRef.child("$name.jpg")

        imagesRef.delete()
            .addOnSuccessListener {
                listener.onComplete(true)
            }
            .addOnFailureListener {
                listener.onComplete(false)
            }
    }
}
