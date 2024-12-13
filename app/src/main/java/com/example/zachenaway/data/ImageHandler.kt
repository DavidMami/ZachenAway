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
    fromGalleryBtn: ImageButton,
) {
    private val galleryLauncher: ActivityResultLauncher<String>

    init {
        galleryLauncher =
            activity.registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                result?.let { image.setImageURI(it) }
            }

        fromGalleryBtn.setOnClickListener { galleryLauncher.launch("image/*") }
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
