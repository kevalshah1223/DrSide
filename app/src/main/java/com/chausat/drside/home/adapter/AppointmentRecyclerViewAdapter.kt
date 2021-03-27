package com.chausat.drside.home.adapter

import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.chausat.drside.R
import com.chausat.drside.home.data.UserDetailsDataClass
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class AppointmentRecyclerViewAdapter :
    RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder>() {

    private val arrayListAppointmentDetails = ArrayList<UserDetailsDataClass>()

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

        private val groupButtonAction = itemView.findViewById<Group>(R.id.groupButtonAction)
        private val textViewApprovedStatus =
            itemView.findViewById<AppCompatTextView>(R.id.textViewApprovedStatus)

        fun onBind(userData: UserDetailsDataClass) {
            when (userData.approved) {
                "approved" -> {
                    groupButtonAction.visibility = View.GONE
                    textViewApprovedStatus.text = itemView.context.getString(R.string.label_appointment_approved)
                }
                "canceled" -> {
                    groupButtonAction.visibility = View.GONE
                    textViewApprovedStatus.text = itemView.context.getString(R.string.label_appointment_canceled)
                }
                else -> {
                    groupButtonAction.visibility = View.VISIBLE
                    textViewApprovedStatus.visibility = View.GONE
                }
            }

            if (userData.gender == "Male") {
                imageViewPatientProfile.setImageResource(R.drawable.male)
            } else {
                imageViewPatientProfile.setImageResource(R.drawable.female)
            }

            textViewPatientName.text = userData.userName
            textViewPatientContact.text = userData.userContact
            textViewPatientAppointment.text = userData.appointmentTime

            buttonAcceptAppointment.setOnClickListener {
                sendSms(userData.userName,userData.userContact, userData.appointmentTime, true)
                setStatus(true, userData.userId)
                groupButtonAction.visibility = View.GONE
                textViewApprovedStatus.text = itemView.context.getString(R.string.label_appointment_approved)
            }

            buttonCancelAppointment.setOnClickListener {
                sendSms(userData.userName,userData.userContact, userData.appointmentTime, false)
                setStatus(false, userData.userId)
                groupButtonAction.visibility = View.GONE
                textViewApprovedStatus.text = itemView.context.getString(R.string.label_appointment_canceled)
            }
        }
    }

    private fun sendSms(userName:String,userNumber: String, appointmentTime: String, isApproved: Boolean) {
        val message = if (isApproved) {
            "Respected $userName,\nYour Appointment for Magnet Therapy is Approved.\nTime: $appointmentTime"
        } else {
            "Respected $userName,\nYour Appointment for Magnet Therapy is Canceled.\nTime: $appointmentTime"
        }
        SmsManager.getDefault().sendTextMessage(
            "$userNumber",
            null, message, null, null
        )
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
        } else {
            "canceled"
        }
        FirebaseDatabase.getInstance().reference.child(TABLE_NAME).child(selectedId.toString())
            .child(
                APPROVED
            ).setValue(status)
    }
}