package com.chausat.drside.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chausat.drside.R
import com.chausat.drside.viewmodel.MainActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

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

        doctorViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        doctorViewModel.fetchDoctorDetails()

        val toggleButtonLanguage =
            view.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonLanguage)
        val textViewAboutUs = view.findViewById<AppCompatTextView>(R.id.textViewAboutUs)
        val buttonEnglish = view.findViewById<MaterialButton>(R.id.buttonEnglish)
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
                    }
                }

                R.id.buttonGujarati -> {
                    if (isChecked) {
                        textViewAboutUs.text = gujju
                    }
                }
            }
        }
    }

}