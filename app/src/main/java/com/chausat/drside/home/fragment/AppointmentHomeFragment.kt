package com.chausat.drside.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chausat.drside.R
import com.chausat.drside.base.BaseFragment
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.home.adapter.AppointmentRecyclerViewAdapter
import com.chausat.drside.viewmodel.MainActivityViewModel

class AppointmentHomeFragment : BaseFragment() {
    private lateinit var recyclerViewAppointment: RecyclerView
    private lateinit var doctorViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        recyclerViewAppointment = view.findViewById(R.id.recyclerViewAppointment)
        doctorViewModel.fetchUserDetails()
    }

    override fun onResume() {
        super.onResume()

        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = resources.getString(R.string.label_appointment)
        val toolImage = (activity as HomeMainActivity).imageViewMainDrawer
        toolImage.setImageResource(R.drawable.ic_menu)
        toolImage.setOnClickListener {
            (activity as HomeMainActivity).openDrawer()
        }
        doctorViewModel.getUserDetails.observe(this, { arrayUserData ->
            recyclerViewAppointment.apply {
                val appointmentRecyclerViewAdapter = AppointmentRecyclerViewAdapter()
                arrayUserData.sortByDescending { it.userId }
                appointmentRecyclerViewAdapter.fillUserDetails(arrayUserData)
                layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
                adapter = appointmentRecyclerViewAdapter
            }
        })
    }
}