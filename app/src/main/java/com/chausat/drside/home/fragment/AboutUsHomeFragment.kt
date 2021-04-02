package com.chausat.drside.home.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.viewmodel.MainActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.firebase.database.FirebaseDatabase

class AboutUsHomeFragment : Fragment() {

    lateinit var doctorViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolTitle = (activity as HomeMainActivity).textViewToolBarTitle
        toolTitle.text = activity!!.getText(R.string.label_about_us)
        val toolImage = (activity as HomeMainActivity).imageViewMainDrawer
        toolImage.setImageResource(R.drawable.ic_menu)
        toolImage.setOnClickListener {
            (activity as HomeMainActivity).openDrawer()
        }

        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        doctorViewModel.fetchDoctorDetails()

        val toggleButtonLanguage =
            view.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonLanguage)
        val textViewAboutUs = view.findViewById<AppCompatTextView>(R.id.textViewAboutUs)
        val buttonEnglish = view.findViewById<MaterialButton>(R.id.buttonEnglish)

        val buttonEditAboutUs =
            view.findViewById<MaterialButton>(R.id.buttonEditAboutUs)

        buttonEnglish.isChecked = true

        var english = ""
        var gujju = ""
        doctorViewModel.getLangAboutUS.observe(this, {
            textViewAboutUs.text = it[0]
            english = it[0]
            gujju = it[1]
        })
        toggleButtonLanguage.addOnButtonCheckedListener { _, checkedId, isChecked ->
            when (checkedId) {
                R.id.buttonEnglish -> {
                    if (isChecked) {
                        textViewAboutUs.text = english
                        buttonEditAboutUs.text = resources.getString(R.string.label_edit)
                    }
                }

                R.id.buttonGujarati -> {
                    if (isChecked) {
                        textViewAboutUs.text = gujju
                        buttonEditAboutUs.text = resources.getString(R.string.label_edit_gujju)
                    }
                }
            }
        }


        val builder = AlertDialog.Builder(activity!!)

        buttonEditAboutUs.setOnClickListener {
            val view =
                activity!!.layoutInflater.inflate(R.layout.dialog_prospection_english, null)
            val editTextProspectionEnglishTop =
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)

            if (buttonEnglish.isChecked) {
                builder.setView(view)
                    .setPositiveButton(R.string.label_edit) { dialog, _ ->
                        dialog.dismiss()
                        isAboutUsEnglish(editTextProspectionEnglishTop.text.toString(), true)
                    }
                editTextProspectionEnglishTop.setText(textViewAboutUs.text)
                builder.create().show()

            } else {
                builder.setView(view)
                    .setPositiveButton(
                        R.string.label_edit_gujju
                    ) { dialog, _ ->
                        dialog.dismiss()
                        isAboutUsEnglish(
                            editTextProspectionEnglishTop.text.toString(),
                            false
                        )
                        buttonEnglish.isChecked = true
                    }
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)
                    .setText(textViewAboutUs.text)
                builder.create().show()
            }
        }
    }

    private fun isAboutUsEnglish(newText: String, isEnglish: Boolean) {
        val firebaseDatabase =
            FirebaseDatabase.getInstance().getReference(CommonTag.personalDetails)
        if (isEnglish) {
            firebaseDatabase.child(CommonTag.aboutUsEnglish).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
        } else {
            firebaseDatabase.child(CommonTag.aboutUsGujarati).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "સફળતાપૂર્વક અપડેટ કર્યું", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

}