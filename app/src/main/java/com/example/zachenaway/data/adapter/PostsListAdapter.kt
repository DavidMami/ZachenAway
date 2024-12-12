package com.example.zachenaway.data.adapter

import com.example.zachenaway.data.database.schema.PostWithUser
import com.example.zachenaway.R

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PostsListAdapter(private val inflater: LayoutInflater, private var data: List<PostWithUser>) :
    RecyclerView.Adapter<PostsListHolder>() {

    fun setPostsList(newItems: List<PostWithUser>) {
        data = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.home_page_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsListHolder {
        val view = inflater.inflate(R.layout.home_page_row, parent, false)
        return PostsListHolder(view, data)
    }

    override fun onBindViewHolder(holder: PostsListHolder, position: Int) {
        if (data.isNotEmpty()) holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}