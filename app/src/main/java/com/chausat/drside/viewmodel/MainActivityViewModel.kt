package com.chausat.drside.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chausat.drside.dataclass.CredentialInfo
import com.chausat.drside.dataclass.DrProfileHomeDataClass
import com.chausat.drside.home.data.UserDetailsDataClass
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
}