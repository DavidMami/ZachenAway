package com.example.zachenaway.ui.menu

import com.example.zachenaway.data.ImageHandler
import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.model.PostModel
import com.example.zachenaway.databinding.FragmentEditPostBinding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zachenaway.data.CountriesClient.getIsraelCities
import java.util.UUID

class EditPostFragment : Fragment() {

    private var binding: FragmentEditPostBinding? = null
    private lateinit var imageLaunchers: ImageHandler
    private var oldPost: Post? = null

    private val city get() = binding?.postCityMaterialAutoCompleteTextView
    private val street get() = binding?.postStreetEditText
    private val description get() = binding?.postDescriptionEditText
    private val category get() = binding?.postCategoryEditText

    private val editPostButton get() = binding?.editPostButton
    private val progressIndicator get() = binding?.editPostPageSpinner

    companion object {
        const val ARG_POST = "post"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPostBinding.inflate(inflater, container, false)

        imageLaunchers = ImageHandler(
            this,
            binding!!.postImageImageView,
            binding!!.uploadImageFromGalleryButton,
            binding!!.takePhotoButton,
        )

        initializeVariables()
        addOnClickListeners()

        return binding!!.root
    }

    private fun initializeVariables() {
        arguments?.let {
            oldPost = it.getSerializable(ARG_POST) as Post

            city?.setText(oldPost?.city)
            street?.setText(oldPost?.street)
            description?.setText(oldPost?.description)
            category?.setText(oldPost?.category)
            imageLaunchers.loadImage(oldPost?.image ?: "")
        }

        loadCitiesIntoAutoCompleteTextView()
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
                        binding?.postCityMaterialAutoCompleteTextView?.setAdapter(adapter)
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
        editPostButton?.setOnClickListener {
            if (!isPostValid()) {
                Toast.makeText(context, "Missing a field", Toast.LENGTH_SHORT).show()
            } else {
                updatePost()
            }
        }
    }

    private fun isPostValid(): Boolean {
        return !(city?.text.isNullOrEmpty() ||
                street?.text.isNullOrEmpty() ||
                description?.text.isNullOrEmpty() ||
                category?.text.isNullOrEmpty())
    }

    private fun updatePost() {
        progressIndicator?.show()

        val post = oldPost?.userId?.let {
            Post(
                oldPost?.id ?: UUID.randomUUID().toString(),
                city?.text.toString(),
                street?.text.toString(),
                description?.text.toString(),
                category?.text.toString(),
                it,
                "",
            )
        }

        PostModel.instance.uploadImage(
            UUID.randomUUID().toString(),
            imageLaunchers.getPhoto()
        ) { url ->
            if (url != null) {
                if (post != null) {
                    post.image = url
                }
            }
            if (post != null) {
                PostModel.instance.updatePost(post)
            }
            progressIndicator?.hide()
            Toast.makeText(context, "Updated post", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
