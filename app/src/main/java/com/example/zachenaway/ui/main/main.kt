package com.example.zachenaway.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavArgument
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.zachenaway.R

import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.ui.auth.SplashScreenActivity

import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        setContentView(R.layout.activity_splash_screen)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_graph_host) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav: BottomNavigationView = findViewById(R.id.mainBottomNavBar)

        NavigationUI.setupWithNavController(bottomNav, navController)
    }

    override fun onStart() {
        super.onStart()

        if (AuthRepository.getInstance().getUser() == null) {
            val intent = Intent(this@Main, SplashScreenActivity::class.java)
            startActivity(intent)
        } else {
            UserModel.instance().setSignedUser()
        }
    }
}
