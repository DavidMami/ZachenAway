package com.example.zachenaway.ui.menu

import com.example.zachenaway.data.adapter.PostsListAdapter
import com.example.zachenaway.data.database.schema.PostWithUser
import com.example.zachenaway.databinding.FragmentHomePageBinding
import com.example.zachenaway.viewmodel.HomePageViewModel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private lateinit var homePageViewModel: HomePageViewModel
    private lateinit var adapter: PostsListAdapter
    private val posts = mutableListOf<PostWithUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        homePageViewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        adapter = PostsListAdapter(layoutInflater, posts)
        binding.PostsList.layoutManager = LinearLayoutManager(context)
        binding.PostsList.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.PostsList.setHasFixedSize(false)
        binding.homePageSpinner.show()

        homePageViewModel.getAllPostsWithUser().observe(viewLifecycleOwner) { updatedPosts ->
            binding.homePageSpinner.hide()
            adapter.setPostsList(updatedPosts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
