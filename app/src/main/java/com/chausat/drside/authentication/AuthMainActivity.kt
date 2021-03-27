package com.chausat.drside.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar

class AuthMainActivity : AppCompatActivity() {

    private lateinit var buttonLogin: AppCompatButton
    private lateinit var editTextPhoneNumber: AppCompatEditText
    private lateinit var editTextPassword: AppCompatEditText
    lateinit var doctorViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_main)

        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        doctorViewModel.fetchCredential()


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener { view ->
            doctorViewModel.getCredential.observe(this, {
                if (editTextPhoneNumber.text.toString() == it.phone_number &&
                    editTextPassword.text.toString() == it.password
                ) {
                    val pref = getSharedPreferences(CommonTag.SharedPreferences, MODE_PRIVATE)
                    val edit = pref.edit()
                    edit.putBoolean(CommonTag.IsLogin, true)
                    edit.apply()
                    startActivity(Intent(this, HomeMainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(view, "Invalid Login, Try Again!", Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }
}