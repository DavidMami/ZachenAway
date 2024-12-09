package com.example.zachenaway.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.zachenaway.R

class SplashScreenFragment : Fragment() {

    companion object {
        fun newInstance(): SplashScreenFragment {
            return SplashScreenFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        val loginBtn: Button = view.findViewById(R.id.loginButton)
        val registerBtn: Button = view.findViewById(R.id.registerButton)

        val authFragmentManager = AuthFragmentManager(requireActivity())

        loginBtn.setOnClickListener {
            authFragmentManager.changeFragment(LoginFragment::class.java)
        }

        registerBtn.setOnClickListener {
            authFragmentManager.changeFragment(RegisterFragment::class.java)
        }

        return view
    }
}
