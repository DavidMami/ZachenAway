package com.example.zachenaway.ui.auth

import com.example.zachenaway.data.ImageHandler
import com.example.zachenaway.data.database.ZachenAwayApplication
import com.example.zachenaway.data.database.schema.User
import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.data.repository.UserFirebaseRepository
import com.example.zachenaway.databinding.FragmentRegisterBinding
import com.example.zachenaway.ui.main.MainActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zachenaway.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageHandler: ImageHandler
    private lateinit var progressIndicator: CircularProgressIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        initializeVariables()
        initializeOnClickListeners()

        return binding.root
    }

    private fun initializeVariables() {
        progressIndicator = binding.progressIndicator
        imageHandler = ImageHandler(
            this,
            binding.userImage,
            binding.uploadUserImageFromGalleryButton,
            binding.takePhotoButton
        )
    }

    private fun initializeOnClickListeners() {
        binding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        binding.signUpButton.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val userAuthentication = AuthRepository.getInstance()
        progressIndicator.show()

        val email = binding.emailInput.text.toString()
        val phone = binding.phoneInput.text.toString()
        val firstName = binding.firstNameInput.text.toString()
        val lastName = binding.lastNameInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (!userAuthentication.isEmailAndPasswordValid(email, password)) {
            progressIndicator.hide()
            Toast.makeText(context, "Email or password is invalid", Toast.LENGTH_SHORT).show()
            return
        }

        if (firstName.isEmpty() || lastName.isEmpty()) {
            progressIndicator.hide()
            Toast.makeText(context, "Name is missing", Toast.LENGTH_SHORT).show()
            return
        }

        userAuthentication.register(email, password) { firebaseUser ->
            if (firebaseUser == null) {
                requireActivity().runOnUiThread {
                    progressIndicator.hide()
                    Toast.makeText(context, "Firebase Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }
                return@register
            }

            val user = firebaseUser.let {
                User(
                    it.uid,
                    email,
                    firstName,
                    lastName,
                    phone
                )
            }

            val userModal = UserFirebaseRepository()

            user.email?.let { email ->
                userModal.uploadImage(email, imageHandler.getPhoto()) { url ->
                    if (url != null) {
                        user.imageUrl = url
                    }

                    UserModel.instance().addUser(user)

                    requireActivity().runOnUiThread {
                        progressIndicator.hide()
                        Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(
                            Intent(
                                ZachenAwayApplication.getMyContext(),
                                MainActivity::class.java
                            )
                        )
                    }
                }
            } ?: run {
                requireActivity().runOnUiThread {
                    progressIndicator.hide()
                    Toast.makeText(context, "Failed to create user object.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            UserModel.instance().setSignedUser()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
