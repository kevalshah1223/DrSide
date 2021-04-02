package com.chausat.drside.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.chausat.drside.R
import com.chausat.drside.base.BaseFragment
import com.chausat.drside.home.HomeMainActivity

class MagnetDetailsHomeFragment : BaseFragment() {

    private lateinit var cardViewDashboard: CardView
    private lateinit var cardViewAddImages: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_magnet_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardViewDashboard = view.findViewById(R.id.cardViewDashboard)
        cardViewAddImages = view.findViewById(R.id.cardViewAddImages)


        cardViewDashboard.setOnClickListener {
            navigator.openFragment(
                fragment = AboutMagnetTherapyFragment(),
                isReplace = true,
                isBackStack = true
            )
        }

        cardViewAddImages.setOnClickListener {
            navigator.openFragment(
                fragment = MagnetImagesFragment(),
                isReplace = true,
                isBackStack = true
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = resources.getString(R.string.magnet_details)
        val toolImage = (activity as HomeMainActivity).imageViewMainDrawer
        toolImage.setImageResource(R.drawable.ic_menu)
        toolImage.setOnClickListener {
            (activity as HomeMainActivity).openDrawer()
        }
    }

}