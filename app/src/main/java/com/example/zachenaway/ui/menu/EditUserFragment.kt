package com.example.zachenaway.ui.menu

import com.example.zachenaway.data.ImageHandler
import com.example.zachenaway.data.database.schema.User
import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.databinding.FragmentEditUserBinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.UUID

class EditUserFragment : Fragment() {

    companion object {
        const val ARG_USER = "user"
    }

    private var user: User? = null
    private var binding: FragmentEditUserBinding? = null
    private lateinit var imageLaunchers: ImageHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(ARG_USER) as User?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)

        imageLaunchers = ImageHandler(
            this,
            binding!!.userImageImageView,
            binding!!.uploadImageFromGalleryButton,
            binding!!.takePhotoButton,
        )

        initializeData()
        return binding!!.root
    }

    private fun initializeData() {
        user?.let {
            imageLaunchers.loadImage(it.imageUrl ?: "")
            binding?.firstNameEditText?.setText(it.firstName)
            binding?.lastNameEditText?.setText(it.lastName)
            binding?.phoneEditText?.setText(it.phone)
        }

        binding?.editUserButton?.setOnClickListener {
            val firstName = binding?.firstNameEditText?.text.toString()
            val lastName = binding?.lastNameEditText?.text.toString()
            val phone = binding?.phoneEditText?.text.toString()

            if (firstName.isBlank() || lastName.isBlank() || phone.isBlank()) {
                Toast.makeText(context, "Field is missing", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            user?.apply {
                this.firstName = firstName
                this.lastName = lastName
                this.phone = phone
            }

            binding?.editUserPageSpinner?.show()

            UserModel.instance().uploadImage(UUID.randomUUID().toString(), imageLaunchers.getPhoto()) { url ->
                user?.imageUrl = url
                user?.let {
                    UserModel.instance().updateUser(it) {
                        binding?.editUserPageSpinner?.hide()
                        Toast.makeText(context, "Updated user", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
