package com.chausat.drside.home.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chausat.drside.CommonTag
import com.chausat.drside.R
import com.chausat.drside.base.BaseFragment
import com.chausat.drside.home.HomeMainActivity
import com.chausat.drside.home.adapter.MagnetGalleryAdapter
import com.chausat.drside.viewmodel.MainActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class MagnetImagesFragment : BaseFragment() {
    private lateinit var recyclerViewMagnetGallery: RecyclerView
    private lateinit var magnetGalleryViewModel: MainActivityViewModel
    private lateinit var magnetGalleryAdapter: MagnetGalleryAdapter
    private lateinit var buttonAddNewImage: FloatingActionButton

    private lateinit var imageUri: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private var fileName: String = ""

    private var arrayListSize = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_magnet_images, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val toolbar = (activity as HomeMainActivity).textViewToolBarTitle
        toolbar.text = resources.getString(R.string.label_add_magnet_images)
        val toolImage = (activity as HomeMainActivity).imageViewMainDrawer
        toolImage.setImageResource(R.drawable.ic_back_arrow)

        toolImage.setOnClickListener {
            activity!!.onBackPressed()
        }

        buttonAddNewImage = view.findViewById(R.id.buttonAddNewImage)

        recyclerViewMagnetGallery = view.findViewById(R.id.recyclerViewMagnetGallery)
        magnetGalleryAdapter = MagnetGalleryAdapter()
        magnetGalleryViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        magnetGalleryViewModel.fetchMagnetTherapyDetails()

        recyclerViewMagnetGallery.apply {
            layoutManager = GridLayoutManager(activity!!, 2, RecyclerView.VERTICAL, false)
            recyclerViewMagnetGallery.adapter = magnetGalleryAdapter
        }
        magnetGalleryViewModel.getMagnetTherapyImages.observe(this, {
            it.removeAt(0)
            magnetGalleryAdapter.fillAdapter(it)
            arrayListSize = it.size
            recyclerViewMagnetGallery.adapter!!.notifyDataSetChanged()
        })

        buttonAddNewImage.setOnClickListener {
            choosePicture()
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
            val alert = AlertDialog.Builder(activity!!)
            val view = LayoutInflater.from(activity!!)
                .inflate(R.layout.custom_dialog_add_image, null)
            view.findViewById<AppCompatImageView>(R.id.imageViewData).setImageURI(imageUri)
            alert.setView(view)
            val alertDialog = alert.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
            view.findViewById<MaterialButton>(R.id.buttonAddImage)
                .setOnClickListener {
                    uploadPicture()
                    alertDialog.dismiss()
                }

            view.findViewById<MaterialButton>(R.id.buttonCancel)
                .setOnClickListener {
                    alertDialog.dismiss()
                }

        }
    }

    private fun uploadPicture() {
        if (arrayListSize != -1) {

            val firebase =
                FirebaseDatabase.getInstance().getReference(CommonTag.magnetTherapy).child("images")

            firebase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var last = "null"
                    for (snap in snapshot.children) {
                        last = snap.key.toString()
                    }
                    Toast.makeText(activity!!, "$last", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            val randomKey = UUID.randomUUID().toString()
            val profileRef = storageReference.child("Magnet Images/${randomKey}")
            profileRef.putFile(imageUri)
                .addOnSuccessListener {
                    FirebaseDatabase.getInstance().getReference(CommonTag.magnetTherapy)
                        .child("images")
                        .child((arrayListSize + 1).toString())
                        .setValue(
                            "https://firebasestorage.googleapis.com/v0/b/ohm-magnet-therapy-and-clinic.appspot.com/o/Magnet%20Images%2F${it.storage.name}?alt=media&token=e728ffd1-23c2-4ff2-b3a0-11d5529a6720"
                        )
                    fileName = it.metadata!!.name.toString()
                }
                .addOnFailureListener {
                    Toast.makeText(activity!!, "failed", Toast.LENGTH_SHORT).show()
                }
            recyclerViewMagnetGallery.adapter!!.notifyDataSetChanged()
        } else {
            Toast.makeText(activity!!, "Try Again!", Toast.LENGTH_SHORT).show()
        }
    }
}