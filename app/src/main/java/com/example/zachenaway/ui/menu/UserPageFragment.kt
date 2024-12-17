package com.example.zachenaway.ui.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zachenaway.data.adapter.UserPostsListAdapter
import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.database.schema.User
import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.databinding.FragmentUserPageBinding
import com.example.zachenaway.ui.auth.SplashScreenActivity
import com.example.zachenaway.viewmodel.UserPageViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.picasso.Picasso

class UserPageFragment : Fragment() {
    private lateinit var mAuth: AuthRepository
    private var binding: FragmentUserPageBinding? = null

    companion object {
        const val USER_KEY = "user"
    }

    private lateinit var userPageViewModel: UserPageViewModel
    private var user: User? = null
    private var userPosts: MutableList<Post> = mutableListOf()

    private lateinit var adapter: UserPostsListAdapter
    private lateinit var progressIndicator: CircularProgressIndicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserPageBinding.inflate(inflater, container, false)
        mAuth = AuthRepository.getInstance()

        initializeVariables()

        binding?.logOutBtn?.setOnClickListener {
            UserModel.instance().signUserOut()
            startActivity(Intent(activity, SplashScreenActivity::class.java))
        }

        binding?.userEditButton?.setOnClickListener {
            user?.let { currentUser ->
                Log.d("QQQQQQ", "this is my message!!!")
                val action = UserPageFragmentDirections.actionUserPageFragmentToEditUserFragment(currentUser)
                Navigation.findNavController(binding!!.root).navigate(action)
            }
            Log.d("WWWWWW", "this is my message!!!")
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeVariables()
        initializeRecycleView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPageViewModel = ViewModelProvider(this)[UserPageViewModel::class.java]
    }

    private fun initializeVariables() {
        with(binding!!) {
            val logOutBtn = logOutBtn
            val userImage = userImage
            val userName = userName

            progressIndicator = userPageSpinner

            initializeUser(userName, userImage)
        }
    }

    private fun initializeUser(userName: TextView, userImage: ImageView) {
        if (user == null) {
            Log.d("DAVID", "User not found!!!")

            val argUser = arguments?.getSerializable(USER_KEY) as? User
            if (argUser == null) {
                UserModel.instance().getSignedUser { u ->
                    if (u != null) {
                        user = u.value
                    }

                    activity?.runOnUiThread {
                        user?.let {
                            userName.text = it.getName()

                            if (!it.imageUrl.isNullOrEmpty()) {
                                Picasso.get().load(it.imageUrl).into(userImage)
                            }
                        }
                    }
                }
            } else {
                user = argUser
                user?.let {
                    userName.text = it.getName()
                    Picasso.get().load(it.imageUrl).into(userImage)
                }
            }
        } else {
            Log.d("DAVID", "User is found!!!")

            userPageViewModel.getUser()?.observe(viewLifecycleOwner) { updatedUser ->
                user = updatedUser

                updatedUser?.let {
                    userName.text = it.getName()
                    Picasso.get().load(it.imageUrl).into(userImage)
                }
            }
        }
    }

    private fun initializeRecycleView() {
        binding?.userPostsList?.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = UserPostsListAdapter(layoutInflater, userPosts)
            this@UserPageFragment.adapter = adapter as UserPostsListAdapter
        }
        progressIndicator.show()
        userPageViewModel.getUserPosts().observe(viewLifecycleOwner) { posts ->
            progressIndicator.hide()
            if (posts != null) {
                userPosts = posts.toMutableList()
            }
            adapter.setPostsList(userPosts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
