package com.example.zachenaway.ui.menu

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.zachenaway.data.network.CountriesClient.getIsraelCities
import com.example.zachenaway.data.ImageHandler
import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.databinding.FragmentCreatePostBinding
import com.example.zachenaway.viewmodel.CreatePostViewModel
import java.util.UUID

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageLaunchers: ImageHandler
    private lateinit var createPostViewModel: CreatePostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        initializeVariables()
        addOnClickListeners()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        createPostViewModel = ViewModelProvider(this).get(CreatePostViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeVariables() {
        with(binding) {
            imageLaunchers = ImageHandler(
                this@CreatePostFragment,
                postImageImageView,
                uploadImageFromGalleryButton,
                takePhotoButton
            )

            loadCitiesIntoAutoCompleteTextView()
        }
    }

    private fun loadCitiesIntoAutoCompleteTextView() {
        context?.let { safeContext ->
            getIsraelCities({ cities ->
                // Check if the fragment is still attached
                if (isAdded) {
                    if (cities.isNotEmpty()) {
                        val adapter = ArrayAdapter(
                            safeContext,
                            android.R.layout.simple_dropdown_item_1line,
                            cities
                        )
                        binding.postCityMaterialAutoCompleteTextView.setAdapter(adapter)
                    } else {
                        Toast.makeText(safeContext, "No cities found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("CreatePostFragment", "Fragment is not attached to activity")
                }
            }, safeContext)
        }
    }

    private fun addOnClickListeners() {
        binding.createPostButton.setOnClickListener {
            if (!isPostValid()) {
                Toast.makeText(context, "Missing a field", Toast.LENGTH_SHORT).show()
            } else {
                createPost()
            }
        }
    }

    private fun isPostValid(): Boolean {
        with(binding) {
            return postStreetEditText.text.toString().isNotEmpty() &&
                    postDescriptionEditText.text.toString().isNotEmpty() &&
                    postCityMaterialAutoCompleteTextView.text.toString().isNotEmpty() &&
                    postCategoryEditText.text.toString().isNotEmpty()
        }
    }

    private fun createPost() {
        binding.createPostPageSpinner.show()

        val post = UserModel.instance().getCurrentUserId()?.let {
            Post(
                id = UUID.randomUUID().toString(),
                city = binding.postCityMaterialAutoCompleteTextView.text.toString(),
                street = binding.postStreetEditText.text.toString(),
                description = binding.postDescriptionEditText.text.toString(),
                category = binding.postCategoryEditText.text.toString(),
                userId = it,
                image = "",
            )
        }

        if (post != null) {
            createPostViewModel.addPost(
                post,
                (binding.postImageImageView.drawable as BitmapDrawable).bitmap
            )
        }

        binding.createPostPageSpinner.hide()
        Toast.makeText(context, "Created post", Toast.LENGTH_SHORT).show()
        Navigation.findNavController(binding.root).popBackStack()
    }
}
