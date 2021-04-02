package com.chausat.drside.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chausat.drside.R
import com.chausat.drside.base.BaseFragment
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.home.adapter.FeedbackRecyclerViewAdapter
import com.chausat.drside.viewmodel.MainActivityViewModel

class FeedbackHomeFragment : BaseFragment() {

    private lateinit var recyclerViewFeedback: RecyclerView
    private lateinit var doctorViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        recyclerViewFeedback = view.findViewById(R.id.recyclerViewFeedback)
        doctorViewModel.fetchFeedbackDetails()
    }

    override fun onResume() {
        super.onResume()

        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = resources.getString(R.string.label_feedback)
        val toolImage = (activity as HomeMainActivity).imageViewMainDrawer
        toolImage.setImageResource(R.drawable.ic_menu)
        toolImage.setOnClickListener {
            (activity as HomeMainActivity).openDrawer()
        }
        doctorViewModel.getFeedbackDetails.observe(this, { arrayFeedbackData ->

            Log.d("FEEDBACK", "onResume: $arrayFeedbackData")

            recyclerViewFeedback.apply {
                val appointmentRecyclerViewAdapter = FeedbackRecyclerViewAdapter()
                arrayFeedbackData.sortByDescending { it.feedbackId }
                appointmentRecyclerViewAdapter.fillFeedbackDetails(arrayFeedbackData)
                layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, false)
                adapter = appointmentRecyclerViewAdapter
            }
        })
    }

}