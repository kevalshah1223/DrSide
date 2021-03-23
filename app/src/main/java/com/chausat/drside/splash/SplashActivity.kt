package com.chausat.drside.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.chausat.drside.R
import com.chausat.drside.authentication.AuthMainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, AuthMainActivity::class.java))
            finish()
        }, 2000)
    }
}