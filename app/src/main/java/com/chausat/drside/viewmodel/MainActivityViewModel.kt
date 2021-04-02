package com.chausat.drside.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chausat.drside.dataclass.CredentialInfo
import com.chausat.drside.dataclass.DrProfileHomeDataClass
import com.chausat.drside.home.data.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivityViewModel : ViewModel() {

    val getCredential: MutableLiveData<CredentialInfo> by lazy {
        MutableLiveData<CredentialInfo>()
    }

    val getDrProfileDetails: MutableLiveData<DrProfileHomeDataClass> by lazy {
        MutableLiveData<DrProfileHomeDataClass>()
    }

    val getUserDetails: MutableLiveData<ArrayList<UserDetailsDataClass>> by lazy {
        MutableLiveData<ArrayList<UserDetailsDataClass>>()
    }

    val getFeedbackDetails: MutableLiveData<ArrayList<FeedbackDataClass>> by lazy {
        MutableLiveData<ArrayList<FeedbackDataClass>>()
    }

    val getLangAboutMagnetTherapy: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>()
    }

    fun fetchCredential() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("login_master")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(CredentialInfo::class.java)
                val c = CredentialInfo()
                c.phone_number = value!!.phone_number
                c.password = value.password
                getCredential.value = c
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun fetchDrInfo() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("personal_details")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(DrProfileHomeDataClass::class.java)
                val drProfileHomeDataClass = DrProfileHomeDataClass()
                drProfileHomeDataClass.name = value!!.name
                drProfileHomeDataClass.profile_image = value.profile_image
                getDrProfileDetails.value = drProfileHomeDataClass
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun fetchUserDetails() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("appointmentDetails")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val arrayListUser = ArrayList<UserDetailsDataClass>()
                for (snap in snapshot.children) {
                    val value = snap.getValue(UserDetailsDataClass::class.java)
                    arrayListUser.add(value!!)
                }
                getUserDetails.value = arrayListUser
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun fetchFeedbackDetails() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("userFeedback")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val arrayListUser = ArrayList<FeedbackDataClass>()
                for (snap in snapshot.children) {
                    val value = snap.getValue(FeedbackDataClass::class.java)
                    arrayListUser.add(value!!)
                }
                getFeedbackDetails.value = arrayListUser
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun fetchProspectionServicesDetails() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("prospection_service")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val magnetTherapyData = snapshot.getValue(ProspectionServicesDataClass::class.java)
                getLangAboutMagnetTherapy.value =
                    arrayListOf(
                        magnetTherapyData!!.about_eng,
                        magnetTherapyData.about_guj,
                        magnetTherapyData.why_eng,
                        magnetTherapyData.why_guj
                    )
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    val getDoctorName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val getClinicName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val getDrImage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val getMagnetImage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val getLangAboutUS: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>()
    }

    val getProfileDetails: MutableLiveData<DoctorDataCLass> by lazy {
        MutableLiveData<DoctorDataCLass>()
    }

    fun fetchDoctorDetails() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("personal_details")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val doctorDetails = snapshot.getValue(DoctorDataCLass::class.java)
                getDoctorName.value = doctorDetails!!.name
                getClinicName.value = doctorDetails.clinic_name
                getDrImage.value = doctorDetails.profile_image
                getMagnetImage.value = doctorDetails.magnet_image
                getLangAboutUS.value =
                    arrayListOf(doctorDetails.about_eng, doctorDetails.about_gujju)
                getProfileDetails.value = doctorDetails
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    val getMagnetTherapyImages: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>()
    }

    fun fetchMagnetTherapyDetails() {
        val firebaseDatabaseReference =
            FirebaseDatabase.getInstance().getReference("magnet_therapy")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val magnetTherapyData = snapshot.getValue(MagnetTherapyDataClass::class.java)
                getLangAboutMagnetTherapy.value =
                    arrayListOf(magnetTherapyData!!.about_eng, magnetTherapyData.about_guj,magnetTherapyData.why_eng, magnetTherapyData.why_guj)

                magnetTherapyData.let {
                    getMagnetTherapyImages.value = it.images
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}