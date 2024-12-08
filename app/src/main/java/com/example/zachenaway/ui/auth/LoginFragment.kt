package com.example.zachenaway.ui.auth

import com.example.zachenaway.ui.main.ZachenAwayApplication
import com.example.zachenaway.ui.main.MainActivity
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.data.model.UserModel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.zachenaway.R

class LoginFragment : Fragment() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val fragmentManager = AuthFragmentManager(requireActivity())
        val userAuthentication = AuthRepository.getInstance()

        email = view.findViewById(R.id.emailInput)
        password = view.findViewById(R.id.passwordInput)
        signInBtn = view.findViewById(R.id.signInBtn)
        signUpBtn = view.findViewById(R.id.SignUpBtn)

        signInBtn.setOnClickListener {
            if (!userAuthentication.isEmailAndPasswordValid(email, password)) {
                Toast.makeText(context, "Email or password is invalid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userAuthentication.login(email, password) { user ->
                if (user != null) {
                    UserModel.instance().setSignedUser()
                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(ZachenAwayApplication.getMyContext(), MainActivity::class.java))
                } else {
                    Toast.makeText(context, "Firebase Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signUpBtn.setOnClickListener {
            fragmentManager.changeFragment(RegisterFragment::class.java)
        }

        return view
    }
}
