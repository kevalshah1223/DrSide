package com.chausat.drside.home.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.home.data.DoctorDetailsDataClass
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var buttonCancelEditProfile: MaterialButton
    private lateinit var buttonSaveEditProfile: MaterialButton

    private lateinit var inputEditTextDrName: TextInputLayout
    private lateinit var inputEditTextDrNumber: TextInputLayout
    private lateinit var inputEditTextDrEmail: TextInputLayout
    private lateinit var inputEditTextDrAddress: TextInputLayout

    private lateinit var imageViewDrImage: AppCompatImageView

    private lateinit var imageUri: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        buttonCancelEditProfile = findViewById(R.id.buttonCancelEditProfile)
        buttonSaveEditProfile = findViewById(R.id.buttonSaveEditProfile)

        inputEditTextDrName = findViewById(R.id.inputEditTextDrName)
        inputEditTextDrNumber = findViewById(R.id.inputEditTextDrNumber)
        inputEditTextDrEmail = findViewById(R.id.inputEditTextDrEmail)
        inputEditTextDrAddress = findViewById(R.id.inputEditTextDrAddress)

        imageViewDrImage = findViewById(R.id.imageViewDrImage)

        val doctorDetails =
            intent.getParcelableExtra<DoctorDetailsDataClass>(CommonTag.doctorDetails)

        inputEditTextDrName.editText!!.setText(doctorDetails!!.name)
        inputEditTextDrNumber.editText!!.setText(doctorDetails.contact_number)
        inputEditTextDrEmail.editText!!.setText(doctorDetails.email)
        inputEditTextDrAddress.editText!!.setText(doctorDetails.address)

        Glide.with(this).load(doctorDetails.profile_image).into(imageViewDrImage)

        imageViewDrImage.setOnClickListener {
            choosePicture()
        }

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
            doctorDetailsDataClass.profile_image =
                "https://firebasestorage.googleapis.com/v0/b/ohm-magnet-therapy-and-clinic.appspot.com/o/Profile%20Image%2F${fileName}?alt=media&token=ac766351-7797-4332-972f-9d4128b41020"

            val intent = Intent(this, HomeMainActivity::class.java)
            intent.putExtra(CommonTag.doctorDetails, doctorDetailsDataClass)
            setResult(CommonTag.editSuccessRequestCode, intent)
            finish()
        }
    }

    private fun choosePicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, CommonTag.imageSuccessRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonTag.imageSuccessRequestCode) {
            imageUri = data!!.data!!
            imageViewDrImage.setImageURI(imageUri)
            uploadPicture()
        }
    }

    private fun uploadPicture() {
        val randomKey = UUID.randomUUID().toString()
        val profileRef = storageReference.child("Profile Image/${randomKey}")
        profileRef.putFile(imageUri)
            .addOnSuccessListener {
                FirebaseDatabase.getInstance().getReference(CommonTag.personalDetails)
                    .child("profile_image")
                    .setValue(
                        "https://firebasestorage.googleapis.com/v0/b/ohm-magnet-therapy-and-clinic.appspot.com/o/Profile%20Image%2F${it.storage.name}?alt=media&token=ac766351-7797-4332-972f-9d4128b41020"
                    )
                fileName = it.metadata!!.name.toString()
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
            }
    }
}