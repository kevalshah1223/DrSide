package com.chausat.drside.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.chausat.drside.Navigator

open class BaseFragment : Fragment() {

    lateinit var navigator: Navigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = activity as Navigator
    }

}