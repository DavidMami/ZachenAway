package com.example.zachenaway.ui.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.zachenaway.R

class AuthFragmentManager(activity: FragmentActivity) {
    private val fragmentManager: FragmentManager = activity.supportFragmentManager
    private val containerId = R.id.auth_nav_graph_host

    fun changeFragment(fragmentClass: Class<out Fragment>, addToBackStack: Boolean = false) {
        val transaction = fragmentManager.beginTransaction()
            .replace(containerId, fragmentClass, null)
            .setReorderingAllowed(true)

        if (addToBackStack) {
            transaction.addToBackStack(fragmentClass.name)
        }

        transaction.commit()
    }
}
