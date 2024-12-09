package com.example.zachenaway.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.zachenaway.R

class AuthFragmentManager(activity: FragmentActivity) {
    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    fun changeFragment(fragmentClass: Class<out Fragment>, addToBackStack: Boolean = false) {
        val transaction = fragmentManager.beginTransaction()
            .replace(R.id.loginHomePageFragment, fragmentClass, null)
            .setReorderingAllowed(true)

        if (addToBackStack) {
            transaction.addToBackStack(fragmentClass.name)
        }

        transaction.commit()
    }
}
