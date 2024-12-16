package com.example.zachenaway.data;

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso

class ImageHandler(
    activity: Fragment,
    private var image: ImageView,
    fromGalleryButton: ImageButton,
    takePhotoButton: ImageButton
) {
    private val cameraLauncher: ActivityResultLauncher<Void?>
    private var galleryLauncher: ActivityResultLauncher<String>

    init {
        cameraLauncher =
            activity.registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { result ->
                result?.let { image.setImageBitmap(it) }
            }

        takePhotoButton.setOnClickListener { cameraLauncher.launch(null) }

        galleryLauncher =
            activity.registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                result?.let { image.setImageURI(it) }
            }

        fromGalleryButton.setOnClickListener { galleryLauncher.launch("image/*") }
    }

    constructor(
        activity: Fragment,
        image: ImageView,
        fromGalleryButton: ImageButton
    ) : this(activity, image, fromGalleryButton, ImageButton(activity.requireContext())) {
        galleryLauncher =
            activity.registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                result?.let { image.setImageURI(it) }
            }

        fromGalleryButton.setOnClickListener { galleryLauncher.launch("image/*") }
    }

    fun getImage(): ImageView {
        return image
    }

    fun setImage(image: ImageView) {
        this.image = image
    }

    fun loadImage(url: String) {
        Picasso.get().load(url).into(image)
    }

    fun getPhoto(): Bitmap {
        image.isDrawingCacheEnabled = true
        image.buildDrawingCache()
        return (image.drawable as BitmapDrawable).bitmap
    }
}
