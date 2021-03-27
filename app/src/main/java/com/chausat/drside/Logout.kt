package com.chausat.drside

import android.content.Context
import android.content.Intent
import com.chausat.drside.authentication.AuthMainActivity

object Logout {
    fun logout(context: Context){
        val pref = context.getSharedPreferences(CommonTag.SharedPreferences,Context.MODE_PRIVATE)
        val edit = pref.edit()
        edit.clear()
        edit.apply()
        context.startActivity(Intent(context, AuthMainActivity::class.java))
    }
}