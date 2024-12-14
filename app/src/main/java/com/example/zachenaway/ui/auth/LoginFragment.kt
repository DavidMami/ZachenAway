package com.example.zachenaway.ui.auth

import com.example.zachenaway.data.database.ZachenAwayApplication
import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.databinding.FragmentLoginBinding
import com.example.zachenaway.ui.main.MainActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val fragmentManager = AuthFragmentManager(requireActivity())
        val userAuthentication = AuthRepository.getInstance()

        binding.signInBtn.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (!userAuthentication.isEmailAndPasswordValid(email, password)) {
                Toast.makeText(context, "Email or password is invalid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userAuthentication.login(email, password) { user ->
                if (user != null) {
                    UserModel.instance().setSignedUser()
                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            ZachenAwayApplication.getMyContext(),
                            MainActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(context, "Firebase Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.SignUpBtn.setOnClickListener {
            fragmentManager.changeFragment(RegisterFragment::class.java)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
