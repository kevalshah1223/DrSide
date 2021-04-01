package com.chausat.drside.home.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.home.data.DoctorDetailsDataClass
import com.chausat.drside.viewmodel.MainActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class ContactUsHomeFragment : Fragment() {

    lateinit var doctorViewModel: MainActivityViewModel
    private lateinit var imageViewDrImage: AppCompatImageView
    private lateinit var textViewDrName: AppCompatTextView
    private lateinit var textViewDrContactNumber: AppCompatTextView
    private lateinit var textViewDrMail: AppCompatTextView
    private lateinit var textViewAddress: AppCompatTextView
    private lateinit var buttonEditProfile: MaterialButton
    private lateinit var doctorDetailsDataClass: DoctorDetailsDataClass

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        doctorDetailsDataClass = DoctorDetailsDataClass()
        textViewDrName = view.findViewById(R.id.textViewDrName)
        textViewDrContactNumber = view.findViewById(R.id.textViewDrContactNumber)
        textViewDrMail = view.findViewById(R.id.textViewDrMail)
        imageViewDrImage = view.findViewById(R.id.imageViewDrImage)
        textViewAddress = view.findViewById(R.id.textViewAddress)
        buttonEditProfile = view.findViewById(R.id.buttonEditProfile)

        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = getString(R.string.label_contact_us)

        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        doctorViewModel.fetchDoctorDetails()

        doctorViewModel.getProfileDetails.observe(this, {
            doctorDetailsDataClass.name = it.name
            doctorDetailsDataClass.email = it.email
            doctorDetailsDataClass.contact_number = it.contact_number
            doctorDetailsDataClass.address = it.address
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

        buttonEditProfile.setOnClickListener {
            val intent = Intent(activity!!, EditProfileActivity::class.java)
            intent.putExtra(CommonTag.doctorDetails, doctorDetailsDataClass)
            startActivityForResult(intent, CommonTag.editSuccessRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == CommonTag.editSuccessRequestCode){
            val docDetails = data!!.getParcelableExtra<DoctorDetailsDataClass>(CommonTag.doctorDetails)
            val firebase = FirebaseDatabase.getInstance().getReference(CommonTag.personalDetails)
            firebase.child("name").setValue(docDetails!!.name)
            firebase.child("contact_number").setValue(docDetails.contact_number)
            firebase.child("email").setValue(docDetails.email)
            firebase.child("address").setValue(docDetails.address)
        }
    }

}