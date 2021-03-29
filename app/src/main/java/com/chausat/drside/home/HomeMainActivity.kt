package com.chausat.drside.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chausat.drside.CommonTag
import com.chausat.drside.Logout
import com.chausat.drside.R
import com.chausat.drside.ServiceChecker
import com.chausat.drside.base.BaseActivity
import com.chausat.drside.home.fragment.AppointmentHomeFragment
import com.chausat.drside.home.fragment.FeedbackHomeFragment
import com.chausat.drside.home.fragment.MagnetDetailsHomeFragment
import com.chausat.drside.home.fragment.ProspectionServicesHomeFragment
import com.chausat.drside.viewmodel.MainActivityViewModel
import com.google.android.material.navigation.NavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class HomeMainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private lateinit var groupMain: Group
    private lateinit var groupInternet: Group


    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayoutMain: DrawerLayout
    private lateinit var imageViewMainDrawer: AppCompatImageView
    private lateinit var imageViewDrawerLogout: AppCompatImageView
    private lateinit var navigationView: NavigationView
    private lateinit var doctorViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                CommonTag.PERMISSION_FLAG = true
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                CommonTag.PERMISSION_FLAG = false
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permission)
            .setPermissions(android.Manifest.permission.SEND_SMS)
            .check()

        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        doctorViewModel.fetchDrInfo()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_home_main)

        groupMain = findViewById(R.id.groupMain)
        groupInternet = findViewById(R.id.groupInternet)
        drawerLayoutMain = findViewById(R.id.drawerLayoutMain)
        imageViewMainDrawer = findViewById(R.id.imageViewMainDrawer)
        imageViewDrawerLogout = findViewById(R.id.imageViewDrawerLogout)
        navigationView = findViewById(R.id.navigationView)

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (ServiceChecker().isConnected(this@HomeMainActivity)!!) {
                    groupMain.visibility = View.VISIBLE
                    groupInternet.visibility = View.GONE
                } else {
                    groupMain.visibility = View.GONE
                    groupInternet.visibility = View.VISIBLE
                }

                handler.postDelayed(this, 1000)

            }
        }, 1000)

        val headerNavigationView = navigationView.getHeaderView(0)
        val imageViewDrProfile =
            headerNavigationView.findViewById<AppCompatImageView>(R.id.imageViewDrProfile)
        val textViewDrName =
            headerNavigationView.findViewById<AppCompatTextView>(R.id.textViewDrName)

        doctorViewModel.getDrProfileDetails.observe(this, {
            textViewDrName.text = it.name
            Glide.with(this).load(it.profile_image).into(imageViewDrProfile)
        })

        toggle =
            ActionBarDrawerToggle(this, drawerLayoutMain, R.string.openDrawer, R.string.closeDrawer)
        drawerLayoutMain.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageViewMainDrawer.setOnClickListener(this)
        imageViewDrawerLogout.setOnClickListener(this)
        navigationView.setNavigationItemSelectedListener(this)

        openFragment(fragment = AppointmentHomeFragment(), isReplace = true, isBackStack = false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuHome -> {
                openFragment(
                    fragment = AppointmentHomeFragment(),
                    isReplace = true,
                    isBackStack = false
                )
            }

            R.id.menuFeedback -> {
                openFragment(
                    fragment = FeedbackHomeFragment(),
                    isReplace = true,
                    isBackStack = true
                )
            }

            R.id.menuLogout -> {
                logout()
            }

            R.id.menuMagnet -> {
                openFragment(
                    fragment = MagnetDetailsHomeFragment(),
                    isReplace = true,
                    isBackStack = true
                )
            }

            R.id.menuProspection -> {
                openFragment(
                    fragment = ProspectionServicesHomeFragment(),
                    isReplace = true,
                    isBackStack = true
                )
            }
        }
        drawerLayoutMain.closeDrawer(GravityCompat.START, true)
        return true
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imageViewMainDrawer -> {
                drawerLayoutMain.openDrawer(GravityCompat.START, true)
            }

            R.id.imageViewDrawerLogout -> {
                logout()
            }
        }
    }

    private fun logout() {
        Logout.logout(this)
    }
}