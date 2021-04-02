package com.chausat.drside.home.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chausat.drside.R

class MagnetGalleryAdapter : RecyclerView.Adapter<MagnetGalleryAdapter.ViewHolder>() {

    private var arrayListImages = ArrayList<String>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewMagnetImage =
            itemView.findViewById<AppCompatImageView>(R.id.imageViewMagnetImage)

        fun onBind(image: String) {
            Glide.with(itemView).load(image).into(imageViewMagnetImage)
            imageViewMagnetImage.setOnClickListener {
                val alert = AlertDialog.Builder(itemView.context)
                val view = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.custom_dialog_image, null)
                Glide.with(itemView).load(image)
                    .into(view.findViewById<AppCompatImageView>(R.id.imageViewData))
                alert.setView(view)
                val alertDialog = alert.create()
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()
                view.findViewById<AppCompatImageView>(R.id.imageViewButtonClose)
                    .setOnClickListener {
                        alertDialog.dismiss()
                    }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_magnet_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = arrayListImages[position]
        holder.onBind(image)
    }

    override fun getItemCount(): Int {
        return arrayListImages.size
    }

    internal fun fillAdapter(arrayListImages: ArrayList<String>) {
        this.arrayListImages = arrayListImages
    }
}