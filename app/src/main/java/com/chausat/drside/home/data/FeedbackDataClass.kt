package com.chausat.drside.home.data

data class FeedbackDataClass(
    val feedbackId: Int,
    val feedbackSubject: String,
    val feedbackReason: String
) {
    constructor() : this(0, "", "")
}
