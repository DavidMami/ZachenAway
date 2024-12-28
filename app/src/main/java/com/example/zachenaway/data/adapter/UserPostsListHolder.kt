package com.example.zachenaway.data.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.zachenaway.R
import com.example.zachenaway.data.Listener
import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.model.PostModel
import com.example.zachenaway.ui.menu.UserPageFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class UserPostsListHolder(itemView: View, data: List<Post>) : RecyclerView.ViewHolder(itemView) {
    private lateinit var post: Post

    private val category: TextView = itemView.findViewById(R.id.post_category)
    private val description: TextView = itemView.findViewById(R.id.post_description)
    private val city: TextView = itemView.findViewById(R.id.post_city)
    private val street: TextView = itemView.findViewById(R.id.post_street)
    private val image: ImageView = itemView.findViewById(R.id.post_image)

    private val editButton: ImageButton = itemView.findViewById(R.id.post_edit_button)
    private val deleteButton: ImageButton = itemView.findViewById(R.id.post_delete_button)

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
        city.text = post.city
        street.text = post.street
        category.text = post.category

        editButton.setOnClickListener {
            Navigation.findNavController(itemView).navigate(
                UserPageFragmentDirections.actionUserPageFragmentToEditPostFragment(post)
            )
        }

        deleteButton.setOnClickListener {
            PostModel.instance.deletePost(post.id, post.id, object : Listener<Unit> {
                override fun onComplete(value: Unit?) {
                    Snackbar.make(itemView, "Post deleted successfully", Snackbar.LENGTH_SHORT)
                        .show()
                }

                fun onError(error: String) {
                    Snackbar.make(itemView, "Failed to delete post: $error", Snackbar.LENGTH_LONG)
                        .show()
                }
            })
        }
    }
}
