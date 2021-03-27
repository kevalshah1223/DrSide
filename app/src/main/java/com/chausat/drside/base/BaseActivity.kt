package com.chausat.drside.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.chausat.drside.Navigator
import com.chausat.drside.R

open class BaseActivity : AppCompatActivity(), Navigator {
    private fun fragmentTransaction(
        fragment: Fragment,
        isReplace: Boolean,
        isBackStack: Boolean,
        bundle: Bundle? = null
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (isReplace) {
            fragmentTransaction.replace(R.id.placeHolder, fragment, fragment::class.java.simpleName)
        } else {
            fragmentTransaction.add(R.id.placeHolder, fragment, fragment::class.java.simpleName)
        }

        if (isBackStack) {
            fragmentTransaction.addToBackStack(fragment::class.java.simpleName)
        }

        bundle?.let {
            fragment.arguments = bundle
        }

        fragmentTransaction.commit()

    }

    override fun openFragment(
        fragment: Fragment,
        isReplace: Boolean,
        isBackStack: Boolean,
        bundle: Bundle?
    ) {
        fragmentTransaction(fragment, isReplace, isBackStack, bundle)
    }
}