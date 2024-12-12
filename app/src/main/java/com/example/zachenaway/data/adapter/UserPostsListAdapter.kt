package com.example.zachenaway.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zachenaway.R
import com.example.zachenaway.data.database.schema.Post

class UserPostsListAdapter(
    private val inflater: LayoutInflater,
    private var data: List<Post>
) : RecyclerView.Adapter<UserPostsListHolder>() {

    fun setPostsList(postsList: List<Post>) {
        data = postsList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.user_page_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostsListHolder {
        val view = inflater.inflate(R.layout.user_page_row, parent, false)
        return UserPostsListHolder(view, data)
    }

    override fun onBindViewHolder(holder: UserPostsListHolder, position: Int) {
        if (data.isNotEmpty()) {
            val post = data[position]
            holder.bind(post)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
