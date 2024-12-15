package com.example.zachenaway.ui.auth

import com.example.zachenaway.databinding.FragmentSplashScreenBinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): SplashScreenFragment {
            return SplashScreenFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        val authFragmentManager = AuthFragmentManager(requireActivity())

//        binding.loginButton.setOnClickListener {
//            authFragmentManager.changeFragment(LoginFragment::class.java)
//        }
//
//        binding.registerButton.setOnClickListener {
//            authFragmentManager.changeFragment(RegisterFragment::class.java)
//        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }
}
