package com.example.zachenaway.ui.menu

import com.example.zachenaway.data.ImageHandler
import com.example.zachenaway.data.database.schema.Post
import com.example.zachenaway.data.model.PostModel
import com.example.zachenaway.databinding.FragmentEditPostBinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.UUID

class EditPostFragment : Fragment() {

    private var binding: FragmentEditPostBinding? = null
    private lateinit var imageLaunchers: ImageHandler
    private var oldPost: Post? = null

    private val location get() = binding?.postLocationEditText
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
            binding!!.uploadPostImageButton,
        )

        initializeVariables()
        addOnClickListeners()

        return binding!!.root
    }

    private fun initializeVariables() {
        arguments?.let {
            oldPost = it.getSerializable(ARG_POST) as Post

            location?.setText(oldPost?.location)
            description?.setText(oldPost?.description)
            category?.setText(oldPost?.category)
            imageLaunchers.loadImage(oldPost?.image ?: "")
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
        return !(location?.text.isNullOrEmpty() ||
                description?.text.isNullOrEmpty() ||
                category?.text.isNullOrEmpty())
    }

    private fun updatePost() {
        progressIndicator?.show()

        val post = oldPost?.userId?.let {
            Post(
                oldPost?.id ?: UUID.randomUUID().toString(),
                location?.text.toString(),
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