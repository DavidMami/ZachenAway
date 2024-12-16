package com.example.zachenaway.data.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.zachenaway.R
import com.example.zachenaway.data.database.schema.Post
import com.squareup.picasso.Picasso

class UserPostsListHolder(itemView: View, data: List<Post>) : RecyclerView.ViewHolder(itemView) {
    private lateinit var post: Post
    private val category: TextView = itemView.findViewById(R.id.post_category)
    private val description: TextView = itemView.findViewById(R.id.post_description)
    private val location: TextView = itemView.findViewById(R.id.post_location)
    private val image: ImageView = itemView.findViewById(R.id.post_image)
    private val editButton: ImageButton = itemView.findViewById(R.id.post_edit_button)

    fun bind(post: Post) {
        this.post = post

        if (post.image.isNotEmpty()) {
            Picasso.get()
                .load(post.image)
                .placeholder(R.drawable.post_image_placeholder)
                .into(image)
        } else {
            Picasso.get()
                .load(R.drawable.post_image_placeholder)
                .into(image)
        }

        description.text = post.description
        location.text = post.location
        category.text = post.category

        // TODO: Handle the edit button listener
    }
}
