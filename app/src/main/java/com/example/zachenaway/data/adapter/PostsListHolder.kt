package com.example.zachenaway.data.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zachenaway.R
import com.example.zachenaway.data.database.schema.PostWithUser
import com.squareup.picasso.Picasso

class PostsListHolder(itemView: View, private val data: List<PostWithUser>) :
    RecyclerView.ViewHolder(itemView) {
    private val userName: TextView = itemView.findViewById(R.id.user_name)
    private val userPhone: TextView = itemView.findViewById(R.id.user_phone)
    private val userImage: ImageView = itemView.findViewById(R.id.user_image)
    private val postDescription: TextView = itemView.findViewById(R.id.post_description)
    private val postLocation: TextView = itemView.findViewById(R.id.post_location)
    private val postImage: ImageView = itemView.findViewById(R.id.post_image)

    fun bind(post: PostWithUser) {
        userName.text = post.user.getName()
        userPhone.text = post.user.phone

        postDescription.text = post.post.description
        postLocation.text = post.post.location

        Picasso.get().load(post.user.imageUrl).into(userImage)
        Picasso.get().load(post.post.image).into(postImage)
    }
}
