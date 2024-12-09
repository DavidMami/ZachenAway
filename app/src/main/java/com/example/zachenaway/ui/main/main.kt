package com.example.zachenaway.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zachenaway.R

import com.example.zachenaway.data.model.UserModel
import com.example.zachenaway.data.repository.AuthRepository
import com.example.zachenaway.ui.auth.SplashScreenActivity

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
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
