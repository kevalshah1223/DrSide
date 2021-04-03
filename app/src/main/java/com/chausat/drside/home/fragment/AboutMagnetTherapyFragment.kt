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

class AboutMagnetTherapyFragment : Fragment() {

    lateinit var magnetViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_magnet_therapy, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = resources.getString(R.string.label_about_magnet_therapy)
        val toolImage = (activity as HomeMainActivity).imageViewMainDrawer
        toolImage.setImageResource(R.drawable.ic_back_arrow)

        toolImage.setOnClickListener {
            activity!!.onBackPressed()
        }

        magnetViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        magnetViewModel.fetchMagnetTherapyDetails()

        val toggleButtonLanguage =
            view.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonLanguage)
        val textViewAboutMagnetTherapyData =
            view.findViewById<AppCompatTextView>(R.id.textViewAboutMagnetTherapyData)
        val buttonEnglish = view.findViewById<MaterialButton>(R.id.buttonEnglish)
        val textViewWhyTherapyText =
            view.findViewById<AppCompatTextView>(R.id.textViewWhyTherapyText)
        val textViewWhyTherapy = view.findViewById<AppCompatTextView>(R.id.textViewWhyTherapy)

        val buttonAboutMagnetEdit =
            view.findViewById<MaterialButton>(R.id.buttonAboutMagnetEdit)
        val buttonWhyMagnetEdit =
            view.findViewById<MaterialButton>(R.id.buttonWhyMagnetEdit)

        buttonEnglish.isChecked = true
        var english = ""
        var gujju = ""

        var why_english = ""
        var why_gujju = ""
        textViewWhyTherapy.text = view.context.getString(R.string.label_why_this_therapy)
        magnetViewModel.getLangAboutMagnetTherapy.observe(this, {
            textViewAboutMagnetTherapyData.text = it[0]
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
                        textViewAboutMagnetTherapyData.text = english
                        textViewWhyTherapy.text =
                            view.context.getString(R.string.label_why_this_therapy)
                        textViewWhyTherapyText.text = why_english
                        buttonAboutMagnetEdit.text = resources.getString(R.string.label_edit)
                    }
                }

                R.id.buttonGujarati -> {
                    if (isChecked) {
                        textViewAboutMagnetTherapyData.text = gujju
                        textViewWhyTherapy.text =
                            view.context.getString(R.string.label_why_this_therapy_guj)
                        textViewWhyTherapyText.text = why_gujju
                        buttonAboutMagnetEdit.text = resources.getString(R.string.label_edit_gujju)
                    }
                }
            }
        }

        val builder = AlertDialog.Builder(activity!!)
        buttonAboutMagnetEdit.setOnClickListener {
            val view =
                activity!!.layoutInflater.inflate(R.layout.dialog_prospection_english, null)

            val editTextProspectionEnglishTop =
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)

            if (buttonEnglish.isChecked) {
                builder.setView(view)
                    .setPositiveButton(R.string.label_edit) { dialog, _ ->
                        dialog.dismiss()
                        isMagnetTopEnglish(editTextProspectionEnglishTop.text.toString(), true)
                    }
                editTextProspectionEnglishTop.setText(textViewAboutMagnetTherapyData.text)
                builder.create().show()

            } else {
                builder.setView(view)
                    .setPositiveButton(
                        R.string.label_edit_gujju
                    ) { dialog, _ ->
                        dialog.dismiss()
                        isMagnetTopEnglish(
                            editTextProspectionEnglishTop.text.toString(),
                            false
                        )
                        buttonEnglish.isChecked = true
                    }
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)
                    .setText(textViewAboutMagnetTherapyData.text)
                builder.create().show()
            }
        }


        buttonWhyMagnetEdit.setOnClickListener {
            val view =
                activity!!.layoutInflater.inflate(R.layout.dialog_prospection_english, null)

            val editTextProspectionEnglishTop =
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)

            if (buttonEnglish.isChecked) {
                builder.setView(view)
                    .setPositiveButton(R.string.label_edit) { dialog, _ ->
                        dialog.dismiss()
                        isMagnetBottomEnglish(editTextProspectionEnglishTop.text.toString(), true)
                    }
                editTextProspectionEnglishTop.setText(textViewWhyTherapyText.text)
                builder.create().show()

            } else {
                builder.setView(view)
                    .setPositiveButton(
                        R.string.label_edit_gujju
                    ) { dialog, _ ->
                        dialog.dismiss()
                        isMagnetBottomEnglish(editTextProspectionEnglishTop.text.toString(), false)
                        buttonEnglish.isChecked = true
                    }
                view.findViewById<AppCompatEditText>(R.id.editTextProspectionEnglishTop)
                    .setText(textViewWhyTherapyText.text)
                builder.create().show()
            }
        }
    }

    private fun isMagnetBottomEnglish(newText: String, isEnglish: Boolean) {
        val firebaseDatabase =
            FirebaseDatabase.getInstance().getReference(CommonTag.magnetTherapy)
        if (isEnglish) {
            firebaseDatabase.child(CommonTag.magnetWhyEnglish).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
        } else {
            firebaseDatabase.child("CommonTag.magnetWhyGujarati").setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "સફળતાપૂર્વક અપડેટ કર્યું", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun isMagnetTopEnglish(newText: String, isEnglish: Boolean) {
        val firebaseDatabase =
            FirebaseDatabase.getInstance().getReference(CommonTag.magnetTherapy)
        if (isEnglish) {
            firebaseDatabase.child(CommonTag.magnetAboutEnglish).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
        } else {
            firebaseDatabase.child(CommonTag.magnetAboutGujarati).setValue(newText)
                .addOnSuccessListener {
                    Toast.makeText(activity!!, "સફળતાપૂર્વક અપડેટ કર્યું", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

}