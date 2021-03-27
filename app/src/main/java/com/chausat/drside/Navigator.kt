package com.chausat.drside

import android.os.Bundle
import androidx.fragment.app.Fragment

interface Navigator {
    fun openFragment(fragment: Fragment, isReplace: Boolean, isBackStack: Boolean, bundle: Bundle? = null)
}