package com.chausat.drside.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.authentication.AuthMainActivity
import com.chausat.drside.home.HomeMainActivity

class  SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed({
            val pref = getSharedPreferences(CommonTag.SharedPreferences, MODE_PRIVATE)
            if(pref.getBoolean(CommonTag.IsLogin, false)){
                startActivity(Intent(this, HomeMainActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this, AuthMainActivity::class.java))
                finish()
            }

        }, 2000)
    }
}