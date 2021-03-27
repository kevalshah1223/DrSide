package com.chausat.drside.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chausat.drside.R
import com.chausat.drside.home.data.UserDetailsDataClass
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AppointmentRecyclerViewAdapter :
    RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder>() {

    private val arrayListAppointmentDetails = ArrayList<UserDetailsDataClass>()
    private lateinit var databaseReference: DatabaseReference

    companion object {
        private const val TABLE_NAME = "appointmentDetails"
        private const val APPROVED = "approved"
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewPatientName =
            itemView.findViewById<AppCompatTextView>(R.id.textViewPatientName)
        private val textViewPatientContact =
            itemView.findViewById<AppCompatTextView>(R.id.textViewPatientContact)
        private val textViewPatientAppointment =
            itemView.findViewById<AppCompatTextView>(R.id.textViewPatientAppointment)
        private val imageViewPatientProfile =
            itemView.findViewById<AppCompatImageView>(R.id.imageViewPatientProfile)
        private val buttonAcceptAppointment =
            itemView.findViewById<MaterialButton>(R.id.buttonAcceptAppointment)
        private val buttonCancelAppointment =
            itemView.findViewById<MaterialButton>(R.id.buttonCancelAppointment)

        fun onBind(userData: UserDetailsDataClass) {
            if (userData.gender == "Male") {
                imageViewPatientProfile.setImageResource(R.drawable.male)
            } else {
                imageViewPatientProfile.setImageResource(R.drawable.female)
            }

            textViewPatientName.text = userData.userName
            textViewPatientContact.text = userData.userContact
            textViewPatientAppointment.text = userData.appointmentTime

            buttonAcceptAppointment.setOnClickListener {
                setStatus(true, userData.userId)
            }

            buttonCancelAppointment.setOnClickListener {
                setStatus(false, userData.userId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_appointment_details, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = arrayListAppointmentDetails[position]
        holder.onBind(userData)
    }

    override fun getItemCount(): Int {
        return arrayListAppointmentDetails.size
    }

    internal fun fillUserDetails(arrayListAppointmentDetails: ArrayList<UserDetailsDataClass>) {
        this.arrayListAppointmentDetails.clear()
        this.arrayListAppointmentDetails.addAll(arrayListAppointmentDetails)
    }

    private fun setStatus(isApproved: Boolean, selectedId: Int) {
        val status = if (isApproved) {
            "approved"
        }else{
            "rejected"
        }
        FirebaseDatabase.getInstance().reference.child(TABLE_NAME).child(selectedId.toString()).child(
            APPROVED).setValue(status)
    }
}