package com.chausat.drside.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chausat.drside.R
import com.chausat.drside.home.data.FeedbackDataClass

class FeedbackRecyclerViewAdapter :
    RecyclerView.Adapter<FeedbackRecyclerViewAdapter.ViewHolder>() {

    private val arrayListFeedbackDataClass = ArrayList<FeedbackDataClass>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textViewSubject =
            itemView.findViewById<AppCompatTextView>(R.id.textViewSubject)
        private val textViewMessage =
            itemView.findViewById<AppCompatTextView>(R.id.textViewMessage)

        fun onBind(feedbackData: FeedbackDataClass) {
            textViewSubject.text = feedbackData.feedbackSubject
            textViewMessage.text = feedbackData.feedbackReason
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_feedback_details, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = arrayListFeedbackDataClass[position]
        holder.onBind(userData)
    }

    override fun getItemCount(): Int {
        return arrayListFeedbackDataClass.size
    }

    internal fun fillFeedbackDetails(arrayListFeedbackDataClass: ArrayList<FeedbackDataClass>) {
        this.arrayListFeedbackDataClass.clear()
        this.arrayListFeedbackDataClass.addAll(arrayListFeedbackDataClass)
    }
}