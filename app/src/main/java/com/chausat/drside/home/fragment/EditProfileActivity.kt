package com.chausat.drside.home.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.home.data.DoctorDataCLass
import com.chausat.drside.home.data.DoctorDetailsDataClass
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var buttonCancelEditProfile: MaterialButton
    private lateinit var buttonSaveEditProfile: MaterialButton

    private lateinit var inputEditTextDrName: TextInputLayout
    private lateinit var inputEditTextDrNumber: TextInputLayout
    private lateinit var inputEditTextDrEmail: TextInputLayout
    private lateinit var inputEditTextDrAddress: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        buttonCancelEditProfile = findViewById(R.id.buttonCancelEditProfile)
        buttonSaveEditProfile = findViewById(R.id.buttonSaveEditProfile)

        inputEditTextDrName = findViewById(R.id.inputEditTextDrName)
        inputEditTextDrNumber = findViewById(R.id.inputEditTextDrNumber)
        inputEditTextDrEmail = findViewById(R.id.inputEditTextDrEmail)
        inputEditTextDrAddress = findViewById(R.id.inputEditTextDrAddress)

        val doctorDetails =
            intent.getParcelableExtra<DoctorDetailsDataClass>(CommonTag.doctorDetails)

        inputEditTextDrName.editText!!.setText(doctorDetails!!.name)
        inputEditTextDrNumber.editText!!.setText(doctorDetails.contact_number)
        inputEditTextDrEmail.editText!!.setText(doctorDetails.email)
        inputEditTextDrAddress.editText!!.setText(doctorDetails.address)



        buttonCancelEditProfile.setOnClickListener {
            setResult(1, Intent(this, HomeMainActivity::class.java))
            finish()
        }

        buttonSaveEditProfile.setOnClickListener {
            val doctorDetailsDataClass = DoctorDetailsDataClass()
            doctorDetailsDataClass.name = inputEditTextDrName.editText!!.text.toString()
            doctorDetailsDataClass.email = inputEditTextDrEmail.editText!!.text.toString()
            doctorDetailsDataClass.address = inputEditTextDrAddress.editText!!.text.toString()
            doctorDetailsDataClass.contact_number = inputEditTextDrNumber.editText!!.text.toString()

            val intent = Intent(this, HomeMainActivity::class.java)
            intent.putExtra(CommonTag.doctorDetails,doctorDetailsDataClass)
            setResult(CommonTag.editSuccessRequestCode, intent)
            finish()
        }
    }
}