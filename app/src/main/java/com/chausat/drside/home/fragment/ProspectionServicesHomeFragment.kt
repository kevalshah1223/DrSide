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

class ProspectionServicesHomeFragment : Fragment() {

    lateinit var prospectionViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_prospection_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = resources.getString(R.string.label_prospection)

        val toggleButtonLanguage =
            view.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonLanguage)
        val textViewAboutProspectionServiceData =
            view.findViewById<AppCompatTextView>(R.id.textViewAboutProspectionServiceData)
        val buttonEnglish = view.findViewById<MaterialButton>(R.id.buttonEnglish)
        val buttonGujarati = view.findViewById<MaterialButton>(R.id.buttonGujarati)
        val textViewWhyTherapyText =
            view.findViewById<AppCompatTextView>(R.id.textViewWhyTherapyText)
        val textViewWhyTherapy = view.findViewById<AppCompatTextView>(R.id.textViewWhyTherapy)

        val buttonEditProspectionTop =
            view.findViewById<MaterialButton>(R.id.buttonEditProspectionTop)
        val buttonEditProspectionBottom =
            view.findViewById<MaterialButton>(R.id.buttonEditProspectionBottom)

        prospectionViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        prospectionViewModel.fetchProspectionServicesDetails()


        buttonEnglish.isChecked = true
        var english = ""
        var gujju = ""

        var why_english = ""
        var why_gujju = ""

        prospectionViewModel.getLangAboutMagnetTherapy.observe(this, {
            textViewAboutProspectionServiceData.text = it[0]
            textViewWhyTherapyText.text = it[2]
            english = it[0]
            gujju = it[1]
            why_english = it[2]
            why_gujju = it[3]
        })

        toggleButtonLanguage.addOnButtonCheckedListener { _, checkedId, isChecked ->
            when (checkedId) {
                R.id.buttonEnglish -> {
                    if (isChecked) {
                        textViewAboutProspectionServiceData.text = english
                        textViewWhyTherapy.text =
                            view.context.getString(R.string.label_why_prospection_eng)
                        textViewWhyTherapyText.text = why_english
                        buttonEditProspectionTop.text = resources.getString(R.string.label_edit)
                        buttonEditProspectionBottom.text = resources.getString(R.string.label_edit)
                    }
                }

                R.id.buttonGujarati -> {
                    if (isChecked) {
                        textViewAboutProspectionServiceData.text = gujju
                        textViewWhyTherapy.text =
                            view.context.getString(R.string.label_why_prospection_guj)
                        textViewWhyTherapyText.text = why_gujju
                        buttonEditProspectionTop.text =
                            resources.getString(R.string.label_edit_gujju)
                        buttonEditProspectionBottom.text = resources.getString(R.string.label_edit_gujju)
                    }
                }
            }
        }

        val builder = AlertDialog.Builder(activity!!)

        buttonEditProspectionTop.setOnClickListener {
            val view =
                activity!!.layoutInflater.inflate(R.layout.dialog_prospection_english, null)
            val editTextProspectionEnglishTop =
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)
            if (buttonEnglish.isChecked) {
                builder.setView(view)
                    .setPositiveButton(R.string.label_edit) { dialog, _ ->
                        dialog.dismiss()
                        isProspectionTopEnglish(editTextProspectionEnglishTop.text.toString(), true)
                    }
                editTextProspectionEnglishTop.setText(textViewAboutProspectionServiceData.text)
                builder.create().show()

            } else {
                builder.setView(view)
                    .setPositiveButton(
                        R.string.label_edit_gujju
                    ) { dialog, _ ->
                        dialog.dismiss()
                        isProspectionTopEnglish(
                            editTextProspectionEnglishTop.text.toString(),
                            false
                        )
                        buttonEnglish.isChecked = true
                    }
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)
                    .setText(textViewAboutProspectionServiceData.text)
                builder.create().show()
            }
        }

        buttonEditProspectionBottom.setOnClickListener {
            val view =
                activity!!.layoutInflater.inflate(R.layout.dialog_prospection_english, null)
            val editTextProspectionEnglishTop =
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)
            if (buttonEnglish.isChecked) {
                builder.setView(view)
                    .setPositiveButton(R.string.label_edit) { dialog, _ ->
                        dialog.dismiss()
                        isProspectionBottomEnglish(editTextProspectionEnglishTop.text.toString(), true)
                    }
            } else {
                builder.setView(view)
                    .setPositiveButton(
                        R.string.label_edit_gujju
                    ) { dialog, _ ->
                        dialog.dismiss()
                        isProspectionBottomEnglish(
                            editTextProspectionEnglishTop.text.toString(),
                            false
                        )
                        buttonEnglish.isChecked = true
                    }
            }
            editTextProspectionEnglishTop.setText(textViewWhyTherapyText.text)
            builder.create().show()
        }
    }

    private fun isProspectionBottomEnglish(newText: String, isEnglish: Boolean) {
        val firebaseDatabase =
            FirebaseDatabase.getInstance().getReference(CommonTag.prospectionService)
        if (isEnglish) {
            firebaseDatabase.child(CommonTag.prospectionAboutWhyEnglish).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
        } else {
            firebaseDatabase.child(CommonTag.prospectionAboutWhyGujarati).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "સફળતાપૂર્વક અપડેટ કર્યું", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun isProspectionTopEnglish(newText: String, isEnglish: Boolean) {
        val firebaseDatabase =
            FirebaseDatabase.getInstance().getReference(CommonTag.prospectionService)
        if (isEnglish) {
            firebaseDatabase.child(CommonTag.prospectionAboutEnglish).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
        } else {
            firebaseDatabase.child(CommonTag.prospectionAboutGujarati).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "સફળતાપૂર્વક અપડેટ કર્યું", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

}