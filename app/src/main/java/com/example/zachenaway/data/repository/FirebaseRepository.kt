package com.example.zachenaway.data.repository

import com.example.zachenaway.data.Listener

import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

abstract class FirebaseRepository {

    protected val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    protected val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()

        db.firestoreSettings = settings
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: Listener<String>) {
        val storageRef: StorageReference = storage.reference
        val imagesRef = storageRef.child("images/$name.jpg")
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
}
