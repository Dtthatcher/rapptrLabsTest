package com.datechnologies.androidtest.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.datechnologies.androidtest.R
import com.datechnologies.androidtest.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splash_screen_background.alpha = 0F
        splash_screen_background.animate().setDuration(1500).alpha(1f).withEndAction {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}