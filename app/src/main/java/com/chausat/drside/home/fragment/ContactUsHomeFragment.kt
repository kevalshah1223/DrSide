package com.chausat.drside.home.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chausat.drside.R
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.viewmodel.MainActivityViewModel

class ContactUsHomeFragment : Fragment() {

    lateinit var doctorViewModel: MainActivityViewModel
    private lateinit var imageViewDrImage: AppCompatImageView
    private lateinit var textViewDrName: AppCompatTextView
    private lateinit var textViewDrContactNumber: AppCompatTextView
    private lateinit var textViewDrMail: AppCompatTextView
    private lateinit var textViewAddress: AppCompatTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewDrName = view.findViewById(R.id.textViewDrName)
        textViewDrContactNumber = view.findViewById(R.id.textViewDrContactNumber)
        textViewDrMail = view.findViewById(R.id.textViewDrMail)
        imageViewDrImage = view.findViewById(R.id.imageViewDrImage)
        textViewAddress = view.findViewById(R.id.textViewAddress)

        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = getString(R.string.label_contact_us)

        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        doctorViewModel.fetchDoctorDetails()

        doctorViewModel.getProfileDetails.observe(this, {
            Glide.with(view.context).load(it.profile_image).into(imageViewDrImage)
            textViewDrName.text = it.name
            textViewDrContactNumber.text = it.contact_number
            textViewDrMail.text = it.email
            textViewAddress.text = it.address
        })

        textViewDrContactNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${textViewDrContactNumber.text}")
            startActivity(intent)
        }

        textViewDrMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:${textViewDrMail.text}")
            startActivity(intent)
        }

    }

}