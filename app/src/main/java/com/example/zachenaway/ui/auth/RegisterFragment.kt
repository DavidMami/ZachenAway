package com.example.zachenaway.ui.auth

import com.example.zachenaway.ui.main.MainActivity
import com.example.zachenaway.data.ImageHandler

import com.example.zachenaway.data.database.schema.User

import com.example.zachenaway.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.data.repository.UserFirebaseRepository
import com.example.zachenaway.ui.main.ZachenAwayApplication
import com.google.android.material.progressindicator.CircularProgressIndicator

class RegisterFragment : Fragment() {
    private lateinit var authFragmentManager: AuthFragmentManager
    private lateinit var imageHandler: ImageHandler
    private lateinit var progressIndicator: CircularProgressIndicator

    private lateinit var email: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var password: EditText
    private lateinit var signUpBtn: Button
    private lateinit var signInBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        authFragmentManager = AuthFragmentManager(requireActivity())

        initializeVariables(view)
        initializeOnClickListeners()

//        imageHandler = ImageHandler(
//            this,
//            view.findViewById(R.id.signUpFragment_userImage),
//            view.findViewById(R.id.signUpFragment_uploadPhoto),
//            view.findViewById(R.id.signUpFragment_takePhoto)
//        )

        return view
    }

    private fun initializeVariables(view: View) {
        email = view.findViewById(R.id.emailInput)
        firstName = view.findViewById(R.id.firstNameInput)
        lastName = view.findViewById(R.id.lastNameInput)
        password = view.findViewById(R.id.passwordInput)
        signUpBtn = view.findViewById(R.id.signUpButton)
        signInBtn = view.findViewById(R.id.signInButton)
        progressIndicator = view.findViewById(R.id.progressIndicator)
    }

    private fun initializeOnClickListeners() {
        signInBtn.setOnClickListener {
            authFragmentManager.changeFragment(LoginFragment::class.java)
        }
        signUpBtn.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val userAuthentication = AuthRepository.getInstance()
        progressIndicator.show()

        if (!userAuthentication.isEmailAndPasswordValid(email, password)) {
            progressIndicator.hide()
            Toast.makeText(context, "Email or password is invalid", Toast.LENGTH_SHORT).show()
            return
        }

        if (firstName.text.toString().isEmpty() || lastName.text.toString().isEmpty()) {
            Toast.makeText(context, "Name is missing", Toast.LENGTH_SHORT).show()
            return
        }

        userAuthentication.register(email, password) { firebaseUser ->
            if (firebaseUser == null) {
                progressIndicator.hide()
                Toast.makeText(context, "Firebase Authentication failed.", Toast.LENGTH_SHORT)
                    .show()
                return@register
            }

            val user = firebaseUser?.let {
                User(
                    it.uid,
                    email.text.toString(),
                    "",
                    firstName.text.toString(),
                    lastName.text.toString()
                )
            }

            val userModal = UserFirebaseRepository()

//            userModal.uploadImage(user.email) { imageHandler.getPhoto() } { url ->
//                if (url != null) user.photoUrl = url
//
//                UserModel.instance().addUser(user)
//            }

            if (user != null) {
                user.email?.let {
                    userModal.uploadImage(it, imageHandler.getPhoto()) { url ->
                        if (url != null) {
                            user.imageUrl = url
                        }

                        UserModel.instance().addUser(user)
                    }
                }
            }


            UserModel.instance().setSignedUser()

            Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(ZachenAwayApplication.getMyContext(), MainActivity::class.java))
        }
    }
}
